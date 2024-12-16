package com.example.Warehouse.services.impl;

import com.example.Warehouse.domain.enums.Status;
import com.example.Warehouse.domain.entities.Purchase;
import com.example.Warehouse.domain.entities.PurchaseItem;
import com.example.Warehouse.domain.entities.Stock;
import com.example.Warehouse.domain.repositories.contracts.purchase.PurchaseRepository;
import com.example.Warehouse.domain.repositories.contracts.user.UserRepository;
import com.example.Warehouse.domain.repositories.contracts.warehouse.WarehouseRepository;
import com.example.Warehouse.models.dto.PageForRedis;
import com.example.Warehouse.models.dto.purchase.PurchaseDto;
import com.example.Warehouse.models.dto.purchase.PurchaseItemDto;
import com.example.Warehouse.exceptions.InvalidDataException;
import com.example.Warehouse.services.contracts.PurchaseService;
import com.example.Warehouse.services.contracts.StockService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PurchaseServiceImpl implements PurchaseService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepo;
    private final StockService stockService;
    private final PurchaseRepository purchaseRepo;
    private final WarehouseRepository warehouseRepo;

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
    @CacheEvict(value = "purchases", allEntries = true)
    public void addPurchase(String username, int pointsSpent) {
        var user = userRepo.findByUsername(username)
            .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        var purchaseItems = user
            .getCart()
            .stream()
            .map(c -> new PurchaseItem(c.getProduct(), c.getQuantity()))
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

        findSuitableWarehouse(
            purchaseItems
                .stream()
                .map(oi -> new PurchaseItemDto(
                    oi.getProduct().getId(),
                    oi.getQuantity())
                ).toList()
        );

        purchaseItems.forEach(item -> item.setPurchase(purchase));
        user.getPurchases().add(purchase);

        user.setPoints(user.getPoints() - pointsSpent + cashback);
        user.getCart().clear();

        userRepo.save(user);
    }

    @Override
    @Cacheable(value = "purchase")
    public PurchaseDto findPurchaseByNumber(String number) {
        var existingPurchase = purchaseRepo.findByNumber(number)
            .orElseThrow(() -> new EntityNotFoundException("Покупка не найдена"));

        return modelMapper.map(existingPurchase, PurchaseDto.class);
    }

    @Override
    @Cacheable(value = "purchases", key = "#username + '-' + #page + '-' + #size")
    public PageForRedis<PurchaseDto> findPurchases(String username, int page, int size) {
        var sortByStatus = Sort.by("status").descending();
        var sortByDate = Sort.by("date").descending();

        Pageable pageable = PageRequest.of(page - 1, size, sortByStatus.and(sortByDate));

        return new PageForRedis<>(purchaseRepo
            .findByUserName(username, pageable)
            .map(o -> modelMapper.map(o, PurchaseDto.class))
        );
    }

    @Override
    @Cacheable(value = "purchases", key = "#page + '-' + #size")
    public PageForRedis<PurchaseDto> findAllPurchases(int page, int size) {
        var sortByStatus = Sort.by("status").descending();
        var sortByDate = Sort.by("date").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sortByStatus.and(sortByDate));

        return new PageForRedis<>(purchaseRepo
            .findAll(pageable).map(p -> modelMapper.map(p, PurchaseDto.class))
        );
    }

    @Override
    @CacheEvict(value = "purchases", allEntries = true)
    public void check(String purchaseNumber) {
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
    @CacheEvict(value = "purchases", allEntries = true)
    public void setCanceled(String purchaseNumber) {
        var purchase = purchaseRepo.findByNumber(purchaseNumber)
            .orElseThrow(() -> new EntityNotFoundException("Заказ с таким номером не найден"));

        purchase.setStatus(Status.CANCELED);
        purchaseRepo.save(purchase);
    }

    private void findSuitableWarehouse(List<PurchaseItemDto> items) {
        var warehouses = warehouseRepo.findAll();

        var totalSuited = 0;
        for (var warehouse : warehouses) {
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
                items.forEach(i ->
                    stockService.decrStockQuantity(
                        warehouse.getId(),
                        i.productId(),
                        i.quantity()
                    )
                );
                break;
            }
            totalSuited = 0;
        }

        if (totalSuited != items.size())
            throw new RuntimeException("Нет подходящего скалда для доставки");
    }

    private String generatePurchaseNumber() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }
}
