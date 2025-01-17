package com.ayesh.inventory.dao;

import com.ayesh.inventory.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface StockDao extends JpaRepository<Stock, Integer>, JpaSpecificationExecutor<Stock> {
}
