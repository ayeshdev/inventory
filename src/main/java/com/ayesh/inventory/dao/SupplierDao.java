package com.ayesh.inventory.dao;

import com.ayesh.inventory.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupplierDao extends JpaRepository<Supplier, Integer> {
    Optional<Supplier> findById(Integer id);
}
