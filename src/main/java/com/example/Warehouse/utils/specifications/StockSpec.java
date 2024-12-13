package com.example.Warehouse.utils.specifications;

import com.example.Warehouse.domain.models.Stock;
import com.example.Warehouse.dto.filters.StockFilter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StockSpec {

    public static Specification<Stock> filter(StockFilter filter) {
        return (root, query, builder) -> {
            List<Predicate> orPredicates = new ArrayList<>();
            List<Predicate> andPredicates = new ArrayList<>();

            Optional.ofNullable(filter.substring())
                .filter(n -> !n.isEmpty())
                .ifPresent(n -> orPredicates.add(
                    builder.like(
                        builder.lower(root.get("product").get("name")),
                        "%" + n.toLowerCase() + "%"
                    )
                ));

            if (filter.category() == null || filter.category().isEmpty()) {
                Optional.ofNullable(filter.substring())
                    .filter(n -> !n.isEmpty())
                    .ifPresent(n -> orPredicates.add(
                        builder.like(
                            builder.lower(root.get("product").get("category").get("name")),
                            "%" + n.toLowerCase() + "%"
                        )
                    ));
            }

            Optional.ofNullable(filter.category())
                .filter(n -> !n.isEmpty())
                .ifPresent(n -> andPredicates.add(
                    builder.like(
                        builder.lower(root.get("product").get("category").get("name")),
                        "%" + n.toLowerCase() + "%"
                    )
                ));

            andPredicates.add(builder.equal(root.get("warehouse").get("id"), filter.warehouseId()));

            if (!orPredicates.isEmpty()) {
                var orPredicate = builder.or(orPredicates.toArray(new Predicate[0]));
                andPredicates.add(orPredicate);
            }

            return builder.and(andPredicates.toArray(new Predicate[0]));
        };
    }
}
