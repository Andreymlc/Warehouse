package com.example.Warehouse.services.impl;

import com.example.Warehouse.domain.entities.Purchase;
import com.example.Warehouse.domain.entities.PurchaseItem;
import com.example.Warehouse.domain.entities.Stock;
import com.example.Warehouse.domain.entities.Warehouse;
import com.example.Warehouse.domain.enums.Status;
import com.example.Warehouse.domain.repositories.contracts.purchase.PurchaseRepository;
import com.example.Warehouse.domain.repositories.contracts.user.UserRepository;
import com.example.Warehouse.domain.repositories.contracts.warehouse.WarehouseRepository;
import com.example.Warehouse.exceptions.InvalidDataException;
import com.example.Warehouse.models.dto.product.ProductCartDto;
import com.example.Warehouse.models.dto.purchase.PurchaseDetails;
import com.example.Warehouse.models.dto.purchase.PurchaseDto;
import com.example.Warehouse.models.dto.purchase.PurchaseItemDto;
import com.example.Warehouse.services.contracts.PurchaseService;
import com.example.Warehouse.services.contracts.StockService;
import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PurchaseServiceImpl implements PurchaseService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepo;
    private final StockService stockService;
    private final PurchaseRepository purchaseRepo;
    private final WarehouseRepository warehouseRepo;

    private static final Logger LOG = LogManager.getLogger(PurchaseServiceImpl.class);

    public PurchaseServiceImpl(
        ModelMapper modelMapper,
        UserRepository userRepo,
        StockService stockService,
        PurchaseRepository purchaseRepo,
        WarehouseRepository warehouseRepo
    ) {
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
        this.stockService = stockService;
        this.purchaseRepo = purchaseRepo;
        this.warehouseRepo = warehouseRepo;
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "cart", key = "#username"),
        @CacheEvict(value = "points", key = "#username"),
        @CacheEvict(value = "purchases", allEntries = true)
    })
    public void addPurchase(String username, int pointsSpent) {
        LOG.info(
            "Cache 'purchases' is cleared. addPurchase called, params: username - {}, pointsSpent - {}",
            username, pointsSpent
        );

        var user = userRepo.findByUsername(username)
            .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        var purchaseItems = user
            .getCart()
            .stream()
            .map(c -> {
                var product = c.getProduct();
                var category = product.getCategory();

                return new PurchaseItem(
                    product,
                    c.getQuantity(),
                    (float) Math.round(product.getPrice() * category.getDiscount() * 100) / 100 * c.getQuantity());
            })
            .toList();

        float totalPrice = 0;
        for (var item : purchaseItems) {
            var product = item.getProduct();
            var category = product.getCategory();

            totalPrice += (float) Math.round(product.getPrice() * category.getDiscount() * 100) / 100 * item.getQuantity();
        }

        if (user.getPoints() < pointsSpent || totalPrice < pointsSpent)
            throw new InvalidDataException("Некорректное значение баллов");

        totalPrice -= pointsSpent;
        var cashback = (int) totalPrice / 10;

        var purchase = new Purchase(
            user,
            Status.CREATED,
            generatePurchaseNumber(),
            totalPrice,
            cashback,
            LocalDateTime.now(),
            pointsSpent,
            purchaseItems
        );
        purchaseItems.forEach(item -> item.setPurchase(purchase));

        findSuitableWarehouse(
            purchaseItems
                .stream()
                .map(oi -> new PurchaseItemDto(
                    oi.getProduct().getId(),
                    oi.getQuantity())
                ).toList()
        );

        user.getPurchases().add(purchase);

        user.setPoints(user.getPoints() - pointsSpent + cashback);
        user.getCart().clear();

        userRepo.save(user);
    }

    @Override
    @Cacheable(value = "purchases", key = "#username + '-' + #page + '-' + #size")
    public Page<PurchaseDto> findPurchases(String username, int page, int size) {
        LOG.info(
            "Cache not found. findPurchases called, username - {}, page - {}, size - {}",
            username, page, size
        );

        var sortByStatus = Sort.by("status").ascending();
        var sortByDate = Sort.by("date").descending();

        Pageable pageable = PageRequest.of(page - 1, size, sortByStatus.and(sortByDate));

        return purchaseRepo
            .findByUserName(username, pageable)
            .map(o -> modelMapper.map(o, PurchaseDto.class));
    }

    @Override
    @Cacheable(value = "purchases", key = "#page + '-' + #size")
    public Page<PurchaseDto> findAllPurchases(int page, int size) {
        LOG.info(
            "Cache not found. findAllPurchases called, page - {}, size - {}",
            page, size
        );

        var sortByStatus = Sort.by("status").ascending();
        var sortByDate = Sort.by("date").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sortByStatus.and(sortByDate));

        return purchaseRepo.findAll(pageable).map(p -> modelMapper.map(p, PurchaseDto.class));
    }

    @Override
    @Caching(evict = {
        @CacheEvict(value = "purchases", allEntries = true),
        @CacheEvict(value = "purchase-details", key = "#purchaseNumber")
    })
    public void check(String purchaseNumber) {
        LOG.info("Cache 'purchases' is cleared. checkPurchase called, purchaseNumber - {}", purchaseNumber);

        var purchase = purchaseRepo.findByNumber(purchaseNumber)
            .orElseThrow(() -> new EntityNotFoundException("Заказ с таким номером не найден"));

        if (purchase.getStatus() == Status.DELIVERED)
            throw new InvalidDataException("У заказа уже финальный статус");
        else {
            purchase.nextStatus();
            purchaseRepo.save(purchase);
        }
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = {"purchases", "points"}, allEntries = true),
        @CacheEvict(value = "purchase-details", key = "#purchaseNumber")
    })
    public void setCanceled(String purchaseNumber) {
        LOG.info("Cache 'purchases' is cleared. setCanceled called, purchaseNumber - {}", purchaseNumber);

        var purchase = purchaseRepo.findByNumber(purchaseNumber)
            .orElseThrow(() -> new EntityNotFoundException("Заказ с таким номером не найден"));

        var user = purchase.getUser();
        var userPoints = user.getPoints();

        if (userPoints >= purchase.getCashback() - purchase.getPointsSpent()) {
            user.setPoints(userPoints - purchase.getCashback() + purchase.getPointsSpent());
        } else {
            user.setPoints(0);
        }

        purchase.setStatus(Status.CANCELED);

        purchaseRepo.save(purchase);
    }

    @Override
    @Cacheable(value = "purchase-details")
    public PurchaseDetails findPurchaseDetails(String number) {
        var purchase = purchaseRepo.findByNumber(number)
            .orElseThrow(() -> new EntityNotFoundException("Покупка не найдена"));

        var items = purchase.getPurchaseItems();
        List<ProductCartDto> productCartDto = new ArrayList<>();

        for (var item : items) {
            var product = item.getProduct();
            var category = product.getCategory();

            productCartDto.add(new ProductCartDto(
                product.getId(),
                product.getName(),
                category.getName(),
                item.getQuantity(),
                item.getTotalPrice()
            ));
        }

        return new PurchaseDetails(
            purchase.getNumber(),
            purchase.getStatus().name(),
            purchase.getTotalPrice(),
            purchase.getCashback(),
            purchase.getDate(),
            purchase.getPointsSpent(),
            productCartDto
        );
    }

    private void findSuitableWarehouse(List<PurchaseItemDto> items) {
        LOG.info("findSuitableWarehouse called, params - {}", items);

        var warehouses = warehouseRepo.findAll();
        List<String> suitableWarehouses = getSuitableWarehouses(items, warehouses);

        if (suitableWarehouses.isEmpty())
            throw new RuntimeException("Нет подходящего склада для доставки");

        Random random = new Random();
        String suitableWarehouse = suitableWarehouses.get(random.nextInt(suitableWarehouses.size()));

        LOG.info("Suitable Warehouse: {}", suitableWarehouse);
        items.forEach(i ->
            stockService.decrStockQuantity(
                suitableWarehouse,
                i.productId(),
                i.quantity()
            )
        );
    }

    private static List<String> getSuitableWarehouses(List<PurchaseItemDto> items, List<Warehouse> warehouses) {
        List<String> suitableWarehouses = new ArrayList<>();

        for (var warehouse : warehouses) {
            var totalSuited = 0;
            Map<String, Integer> stoksMap = new HashMap<>();

            for (Stock stock : warehouse.getStocks()) {
                stoksMap.put(stock.getProduct().getId(), stock.getQuantity());
            }

            for (var item : items) {
                if (stoksMap.containsKey(item.productId()) && stoksMap.get(item.productId()) >= item.quantity()) {
                    totalSuited++;
                }
            }

            if (totalSuited == items.size()) {
                suitableWarehouses.add(warehouse.getId());
            }
        }
        return suitableWarehouses;
    }

    private String generatePurchaseNumber() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }
}
