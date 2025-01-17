package com.ayesh.inventory.controller;

import com.ayesh.inventory.entity.Sales;
import com.ayesh.inventory.exception.SalesNotFoundException;
import com.ayesh.inventory.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("sales")
public class SalesController {
    @Autowired
    SalesService salesService;

    @GetMapping("/allSales")
    public ResponseEntity<List<Sales>> getAllSales() {
        return salesService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sales> getSale(@PathVariable Integer id) {
        return salesService.getSale(id);
    }

    @PostMapping("/addSale")
    public ResponseEntity<Sales> addSale(@RequestBody Sales sale) {
        return salesService.addSale(sale);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sales> updateSale(@RequestBody Sales sale) {
        if(sale.getId() == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        try{
            return salesService.updateSale(sale);
        }catch (SalesNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // Supplier not found
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); // Internal error
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSale(@PathVariable Integer id) {
        return salesService.deleteSale(id);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Sales>> searchSales(
            @RequestParam(required = false) String productCode,
            @RequestParam(required = false) String customerCode,
            @RequestParam(required = false) String soldBy,
            @RequestParam(required = false) Date date) {
        return salesService.searchSales(productCode, customerCode, soldBy, date);
    }
}
