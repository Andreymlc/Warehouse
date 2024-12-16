package com.example.Warehouse.utils.specifications;

import com.example.Warehouse.domain.models.Warehouse;
import com.example.Warehouse.dto.filters.WarehouseFilter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WarehouseSpec {

    public static Specification<Warehouse> filter(WarehouseFilter filter) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> orPredicates = new ArrayList<>();
            List<Predicate> andPredicates = new ArrayList<>();

            Optional.ofNullable(filter.substring())
                .filter(w -> !w.isEmpty())
                .ifPresent(w -> orPredicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + filter.substring() + "%"
                    )
                ));

            Optional.ofNullable(filter.substring())
                .filter(w -> !w.isEmpty())
                .ifPresent(w -> orPredicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("location")),
                        "%" + filter.substring() + "%"
                    )
                ));

            if (!filter.returnDeleted())
                andPredicates.add(criteriaBuilder.isFalse(root.get("isDeleted")));

            if (!orPredicates.isEmpty()) {
                var orPredicate = criteriaBuilder.or(orPredicates.toArray(new Predicate[0]));
                andPredicates.add(orPredicate);
            }

            return criteriaBuilder.and(andPredicates.toArray(new Predicate[0]));
        };
    }
}
