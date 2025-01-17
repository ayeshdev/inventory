package com.ayesh.inventory.dao;

import com.ayesh.inventory.entity.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesDao extends JpaRepository<Sales,Integer> , JpaSpecificationExecutor<Sales> {
}
