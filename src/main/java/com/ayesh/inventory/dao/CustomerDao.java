package com.ayesh.inventory.dao;

import com.ayesh.inventory.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CustomerDao extends JpaRepository<Customer, Integer> , JpaSpecificationExecutor<Customer> {
}
