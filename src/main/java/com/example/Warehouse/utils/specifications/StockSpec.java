package com.example.Warehouse.utils.specifications;

import com.example.Warehouse.domain.entities.Stock;
import com.example.Warehouse.models.filters.StockFilter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StockSpec {

    public static Specification<Stock> filter(StockFilter filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> orPredicates = new ArrayList<>();
            List<Predicate> andPredicates = new ArrayList<>();

            Optional.ofNullable(filter.substring())
                .filter(s -> !s.isEmpty())
                .ifPresent(n -> orPredicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("product").get("name")),
                        "%" + n.toLowerCase() + "%"
                    )
                ));

            if (filter.category() == null || filter.category().isEmpty()) {
                Optional.ofNullable(filter.substring())
                    .filter(c -> !c.isEmpty())
                    .ifPresent(n -> orPredicates.add(
                        criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("product").get("category").get("name")),
                            "%" + n.toLowerCase() + "%"
                        )
                    ));
            }

            Optional.ofNullable(filter.category())
                .filter(c -> !c.isEmpty())
                .ifPresent(n -> andPredicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("product").get("category").get("name")),
                        "%" + n.toLowerCase() + "%"
                    )
                ));

            if (!filter.returnDeletedProduct())
                andPredicates.add(criteriaBuilder.isFalse(root.get("product").get("isDeleted")));

            andPredicates.add(criteriaBuilder.equal(root.get("warehouse").get("id"), filter.warehouseId()));

            if (!orPredicates.isEmpty()) {
                var orPredicate = criteriaBuilder.or(orPredicates.toArray(new Predicate[0]));
                andPredicates.add(orPredicate);
            }

            return criteriaBuilder.and(andPredicates.toArray(new Predicate[0]));
        };
    }
}
