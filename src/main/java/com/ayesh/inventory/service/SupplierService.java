package com.ayesh.inventory.service;

import com.ayesh.inventory.dao.SupplierDao;
import com.ayesh.inventory.entity.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {

    @Autowired
    SupplierDao supplierDao;

    public ResponseEntity<List<Supplier>> getAllSuppliers() {
        try{
            List<Supplier> suppliers = supplierDao.findAll();
            System.out.println("Fetched Suppliers: " + suppliers.toString()); // Debug
            return new ResponseEntity<>(suppliers, HttpStatus.OK);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<Supplier> addSupplier(Supplier supplier) {
        try{
            supplierDao.save(supplier);
            return new ResponseEntity<>(supplier, HttpStatus.OK);
        }catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<Supplier> updateSupplier(Supplier supplier) {

        try {
            Supplier existingSupplier = supplierDao.findById(supplier.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Supplier not found"));

            existingSupplier.setFullName(supplier.getFullName());
            existingSupplier.setLocation(supplier.getLocation());
            existingSupplier.setPhone(supplier.getPhone());
            existingSupplier.setSupplierCode(supplier.getSupplierCode());

            Supplier updatedSupplier = supplierDao.save(existingSupplier);
            return new ResponseEntity<>(updatedSupplier, HttpStatus.OK);
        }catch(IllegalArgumentException e) {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }catch(Exception e) {
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Supplier> getSupplier(Integer id) {
        try {
            Supplier supplier = supplierDao.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Supplier not found"));

            return new ResponseEntity<>(supplier, HttpStatus.OK);
        }catch(IllegalArgumentException e) {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }catch(Exception e) {
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> deleteSupplier(Integer id) {
        try {
            Supplier supplier = supplierDao.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Supplier not found"));
            supplierDao.deleteById(id);
            return new ResponseEntity<>(supplier.getFullName() + " is deleted successfully!", HttpStatus.OK);
        }catch(IllegalArgumentException e) {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }catch(Exception e) {
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
