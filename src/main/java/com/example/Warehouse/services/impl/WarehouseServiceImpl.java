package com.example.Warehouse.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import com.example.Warehouse.domain.models.Warehouse;
import com.example.Warehouse.services.WarehouseService;
import com.example.WarehouseContracts.dto.WarehouseDto;
import com.example.WarehouseContracts.dto.WarehouseAddDto;
import com.example.Warehouse.domain.repository.WarehouseRepository;

@Service
public class WarehouseServiceImpl implements WarehouseService {
    private final ModelMapper modelMapper;
    private final WarehouseRepository warehouseRepo;

    public WarehouseServiceImpl(WarehouseRepository warehouseRepo, ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.warehouseRepo = warehouseRepo;
    }

    @Override
    public Page<WarehouseDto> getWarehouses(String substring, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("name"));

        Page<Warehouse> warehouses = substring != null
                ? warehouseRepo.findByNameContainingIgnoreCase(substring, pageable)
                : warehouseRepo.findAll(pageable);


        return warehouses.map(w ->
            new com.example.WarehouseContracts.dto.WarehouseDto(
                w.getId(),
                w.getName(),
                w.getLocation()
            )
        );
    }

    @Override
    public WarehouseDto getById(String id) {
        var warehouse = warehouseRepo.findById(id).orElseThrow();

        return modelMapper.map(warehouse, com.example.WarehouseContracts.dto.WarehouseDto.class);
    }

    @Override
    public String add(WarehouseAddDto warehouseDto) {
         return warehouseRepo.save(modelMapper.map(warehouseDto, Warehouse.class)).getId();
    }

    @Override
    public void delete(String warehouseId) {
        warehouseRepo.deleteById(warehouseId);
    }
}
