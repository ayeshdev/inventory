package com.ayesh.inventory.dao;

import com.ayesh.inventory.entity.Product;
import com.ayesh.inventory.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseDao extends JpaRepository<Purchase, Integer>, JpaSpecificationExecutor<Purchase> {
}
