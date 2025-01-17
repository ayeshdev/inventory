package com.ayesh.inventory.controller;

import com.ayesh.inventory.entity.Stock;
import com.ayesh.inventory.exception.StockNotFoundException;
import com.ayesh.inventory.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("stock")
public class StockController {
    @Autowired
    StockService stockService;

    @GetMapping("/allStocks")
    public ResponseEntity<List<Stock>> getAllStocks() {
        return stockService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Stock> getStock(@PathVariable Integer id) {
        return stockService.getStock(id);
    }

    @PostMapping("/addStock")
    public ResponseEntity<Stock> addStock(@RequestBody Stock stock) {
        return stockService.addStock(stock);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Stock> updateStock(@RequestBody Stock stock) {
        if(stock.getId() == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        try{
            return stockService.updateStock(stock);
        }catch (StockNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // Supplier not found
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); // Internal error
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStock(@PathVariable Integer id) {
        return stockService.deleteStock(id);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Stock>> searchStocks(
            @RequestParam(required = false) Integer quantity) {
        return stockService.searchStocks(quantity);
    }
}
