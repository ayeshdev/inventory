package com.ayesh.inventory.specification;

import com.ayesh.inventory.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<User> hasUserName(String userName) {
        return (root, query, criteriaBuilder) ->
                userName == null ? null : criteriaBuilder.like(root.get("userName"), "%" + userName + "%");
    }

    public static Specification<User> hasFullName(String fullName) {
        return (root, query, criteriaBuilder) ->
                fullName == null ? null : criteriaBuilder.like(root.get("fullName"), "%" + fullName + "%");
    }

    public static Specification<User> hasEmail(String email) {
        return (root, query, criteriaBuilder) ->
                email == null ? null : criteriaBuilder.like(root.get("email"), "%" + email + "%");
    }

    public static Specification<User> hasUserCategory(String userCategory) {
        return (root, query, criteriaBuilder) ->
                userCategory == null ? null : criteriaBuilder.equal(root.get("userCategory"), userCategory);
    }
}
