package com.ayesh.inventory.specification;

import com.ayesh.inventory.entity.Product;
import com.ayesh.inventory.entity.Purchase;
import org.springframework.data.jpa.domain.Specification;

public class PurchaseSpecification {
    public static Specification<Purchase> hasProductCode(String productCode) {
        return (root, query, criteriaBuilder) ->
                productCode == null ? null : criteriaBuilder.like(root.get("productCode"), "%" + productCode + "%");
    }

    public static Specification<Purchase> hasSupplierCode(String supplierCode) {
        return (root, query, criteriaBuilder) ->
                supplierCode == null ? null : criteriaBuilder.equal(root.get("supplierCode"), supplierCode);
    }
}
