package com.ayesh.inventory.service;

import com.ayesh.inventory.dao.PurchaseDao;
import com.ayesh.inventory.entity.Product;
import com.ayesh.inventory.entity.Purchase;
import com.ayesh.inventory.specification.ProductSpecification;
import com.ayesh.inventory.specification.PurchaseSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PurchaseService {

    @Autowired
    PurchaseDao purchaseDao;

    public ResponseEntity<List<Purchase>> getAllPurchase() {
        try{
            return new ResponseEntity<>(purchaseDao.findAll(), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong");
        }
    }

    public ResponseEntity<Purchase> getPurchaseById(Integer id) {
        try {
            Purchase purchase = purchaseDao.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Purchase not found"));

            return new ResponseEntity<>(purchase, HttpStatus.OK);
        }catch(IllegalArgumentException e) {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }catch(Exception e) {
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Purchase> addPurchase(Purchase purchase) {
        try{
            purchaseDao.save(purchase);
            return new ResponseEntity<>(purchase, HttpStatus.OK);
        }catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<Purchase> updatePurchase(Purchase purchase) {
        try {
            Purchase existingPurchase = purchaseDao.findById(purchase.getPurchaseId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));

            existingPurchase.setProductCode(purchase.getProductCode());
            existingPurchase.setQuantity(purchase.getQuantity());
            existingPurchase.setSupplierCode(purchase.getSupplierCode());
            existingPurchase.setTotalCost(purchase.getTotalCost());

            Purchase updatedPurchase = purchaseDao.save(existingPurchase);
            return new ResponseEntity<>(updatedPurchase, HttpStatus.OK);
        }catch(IllegalArgumentException e) {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }catch(Exception e) {
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> deletePurchase(Integer id) {
        try {
            Purchase purchase = purchaseDao.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Purchase not found"));
            purchaseDao.deleteById(id);
            return new ResponseEntity<>(purchase.getProductCode()+ " is deleted successfully!", HttpStatus.OK);
        }catch(IllegalArgumentException e) {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }catch(Exception e) {
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Cacheable(value = "purchases", key = "#productCode + #supplierCode")
    public ResponseEntity<List<Purchase>> searchPurchase(String productCode, String supplierCode) {
        try {
            Specification<Purchase> specification = Specification.where(
                    PurchaseSpecification.hasProductCode(productCode)
                            .and(PurchaseSpecification.hasSupplierCode(supplierCode))
            );

            List<Purchase> purchases = purchaseDao.findAll(specification);
            return new ResponseEntity<>(purchases, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while searching for purchases", e);
        }
    }
}
