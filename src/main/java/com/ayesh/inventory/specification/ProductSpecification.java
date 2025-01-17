package com.ayesh.inventory.specification;

import com.ayesh.inventory.entity.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {
    public static Specification<Product> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                name == null ? null : criteriaBuilder.like(root.get("productName"), "%" + name + "%");
    }

    public static Specification<Product> hasCode(String code) {
        return (root, query, criteriaBuilder) ->
                code == null ? null : criteriaBuilder.equal(root.get("productCode"), code);
    }

    public static Specification<Product> hasBrand(String brand) {
        return (root, query, criteriaBuilder) ->
                brand == null ? null : criteriaBuilder.equal(root.get("brand"), brand);
    }
}
