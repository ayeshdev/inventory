package com.ayesh.inventory.specification;
import com.ayesh.inventory.entity.Sales;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class SalesSpecification {
    public static Specification<Sales> hasProductCode(String productCode) {
        return (root, query, criteriaBuilder) ->
                productCode == null ? null : criteriaBuilder.like(root.get("productCode"), "%" + productCode + "%");
    }

    public static Specification<Sales> hasCustomerCode(String customerCode) {
        return (root, query, criteriaBuilder) ->
                customerCode == null ? null : criteriaBuilder.equal(root.get("customerCode"), customerCode);
    }

    public static Specification<Sales> hasSoldBy(String soldBy) {
        return (root, query, criteriaBuilder) ->
                soldBy == null ? null : criteriaBuilder.equal(root.get("soldBy"), soldBy);
    }

    public static Specification<Sales> hasDate(Date date) {
        return (root, query, criteriaBuilder) ->
                date == null ? null : criteriaBuilder.equal(root.get("date"), date);
    }
}
