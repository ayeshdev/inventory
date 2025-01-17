package com.ayesh.inventory.specification;

import com.ayesh.inventory.entity.Product;
import com.ayesh.inventory.entity.Stock;
import org.springframework.data.jpa.domain.Specification;

public class StockSpecification {
    public static Specification<Stock> hasQuantity(Integer quantity) {
        return (root, query, criteriaBuilder) ->
                quantity == null ? null : criteriaBuilder.like(root.get("quantity"), "%" + quantity + "%");
    }
}
