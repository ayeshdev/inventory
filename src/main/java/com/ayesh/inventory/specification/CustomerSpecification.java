package com.ayesh.inventory.specification;

import com.ayesh.inventory.entity.Customer;
import org.springframework.data.jpa.domain.Specification;

public class CustomerSpecification {
    public static Specification<Customer> hasCustomerName(String customerName) {
        return (root, query, criteriaBuilder) ->
                customerName == null ? null : criteriaBuilder.like(root.get("customerName"), "%" + customerName + "%");
    }

    public static Specification<Customer> hasCustomerPhone(String customerPhone) {
        return (root, query, criteriaBuilder) ->
                customerPhone == null ? null : criteriaBuilder.equal(root.get("customerPhone"), customerPhone);
    }
}
