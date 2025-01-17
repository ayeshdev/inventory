package com.ayesh.inventory.controller;

import com.ayesh.inventory.entity.Product;
import com.ayesh.inventory.entity.Purchase;
import com.ayesh.inventory.exception.PurchaseNotFoundException;
import com.ayesh.inventory.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("purchase")
public class PurchaseController {

    @Autowired
    PurchaseService purchaseService;

    @GetMapping("/getAll")
    public ResponseEntity<List<Purchase>> getAllPurchase(){
        return purchaseService.getAllPurchase();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Purchase> getPurchaseById(@PathVariable Integer id){
        return purchaseService.getPurchaseById(id);
    }

    @PostMapping("/addPurchase")
    public ResponseEntity<Purchase> addPurchase(@RequestBody Purchase purchase){
        return purchaseService.addPurchase(purchase);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Purchase> updatePurchase(@RequestBody Purchase purchase){
        if(purchase.getPurchaseId() == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        try{
            return purchaseService.updatePurchase(purchase);
        }catch (PurchaseNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // Supplier not found
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); // Internal error
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePurchase(@PathVariable Integer id) {
        return purchaseService.deletePurchase(id);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Purchase>> searchProducts(
            @RequestParam(required = false) String productCode,
            @RequestParam(required = false) String supplierCode) {
        return purchaseService.searchPurchase(productCode, supplierCode);
    }
}
