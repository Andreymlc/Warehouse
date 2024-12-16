package com.example.Warehouse.utils.specifications;

import com.example.Warehouse.domain.models.Category;
import com.example.Warehouse.dto.filters.CategoryFilter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategorySpec {

    public static Specification<Category> filter(CategoryFilter filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            Optional.ofNullable(filter.substring())
                .filter(n -> !n.isEmpty())
                .ifPresent(n -> predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + n.toLowerCase() + "%"
                    )
                ));

            if (!filter.deleted())
                predicates.add(criteriaBuilder.isFalse(root.get("isDeleted")));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
