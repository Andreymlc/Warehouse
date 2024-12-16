package com.example.Warehouse.utils.specifications;

import com.example.Warehouse.domain.models.Product;
import com.example.Warehouse.dto.filters.ProductFilter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductSpec {

    public static Specification<Product> filter(ProductFilter filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> orPredicates = new ArrayList<>();
            List<Predicate> andPredicates = new ArrayList<>();

            Optional.ofNullable(filter.substring())
                .filter(n -> !n.isEmpty())
                .ifPresent(n -> orPredicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + n.toLowerCase() + "%"
                    )
                ));

            if (filter.category() == null || filter.category().isEmpty()) {
                Optional.ofNullable(filter.substring())
                    .filter(n -> !n.isEmpty())
                    .ifPresent(n -> orPredicates.add(
                        criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("category").get("name")),
                            "%" + n.toLowerCase() + "%"
                        )
                    ));
            }

            Optional.ofNullable(filter.category())
                .filter(c -> !c.isEmpty())
                .ifPresent(c -> andPredicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("category").get("name")),
                        "%" + c.toLowerCase() + "%"
                    )
                ));

            if (!filter.deleted())
                andPredicates.add(criteriaBuilder.isFalse(root.get("isDeleted")));

            if (!orPredicates.isEmpty()) {
                var orPredicate = criteriaBuilder.or(orPredicates.toArray(new Predicate[0]));
                andPredicates.add(orPredicate);
            }

            return criteriaBuilder.and(andPredicates.toArray(new Predicate[0]));
        };
    }
}
