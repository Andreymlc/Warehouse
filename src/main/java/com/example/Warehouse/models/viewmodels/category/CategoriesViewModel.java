package com.example.Warehouse.models.viewmodels.category;

import com.example.Warehouse.models.viewmodels.base.BasePagesViewModel;

import java.util.List;

public record CategoriesViewModel(
        BasePagesViewModel base,
        List<CategoryViewModel> categories
) {}
