package com.ayesh.inventory.service;

import com.ayesh.inventory.dao.StockDao;
import com.ayesh.inventory.entity.Stock;
import com.ayesh.inventory.specification.StockSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {
    @Autowired
    StockDao stockDao;

    public ResponseEntity<List<Stock>> getAll() {
        try {
            List<Stock> stocks = stockDao.findAll();
            System.out.println("Fetched Stocks: " + stocks.toString()); // Debug
            return new ResponseEntity<>(stocks, HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<Stock> getStock(Integer id) {
        try {
            Stock stock = stockDao.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Stock not found"));

            return new ResponseEntity<>(stock, HttpStatus.OK);
        }catch(IllegalArgumentException e) {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }catch(Exception e) {
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<Stock> addStock(Stock stock) {
        try{
            stockDao.save(stock);
            return new ResponseEntity<>(stock, HttpStatus.OK);
        }catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<Stock> updateStock(Stock stock) {
        try {
            Stock existingStock = stockDao.findById(stock.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Stock not found"));

            existingStock.setProductId(stock.getProductId());
            existingStock.setQuantity(stock.getQuantity());
            existingStock.setMinimumQuantity(stock.getMinimumQuantity());


            Stock updatedStock = stockDao.save(existingStock);
            return new ResponseEntity<>(updatedStock, HttpStatus.OK);
        }catch(IllegalArgumentException e) {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }catch(Exception e) {
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> deleteStock(Integer id) {
        try {
            Stock stock = stockDao.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Stock not found"));
            stockDao.deleteById(id);
            return new ResponseEntity<>(stock.getId() + " is deleted successfully!", HttpStatus.OK);
        }catch(IllegalArgumentException e) {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }catch(Exception e) {
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Cacheable(value = "stocks", key = "#quantity")
    public ResponseEntity<List<Stock>> searchStocks(Integer quantity) {
        try {
            Specification<Stock> specification = Specification.where(
                    StockSpecification.hasQuantity(quantity)
            );

            List<Stock> stocks = stockDao.findAll(specification);
            return new ResponseEntity<>(stocks, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while searching for stocks", e);
        }
    }

}
