package com.ayesh.inventory.service;

import com.ayesh.inventory.dao.SalesDao;
import com.ayesh.inventory.entity.Sales;
import com.ayesh.inventory.specification.SalesSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SalesService {
    @Autowired
    SalesDao salesDao;

    public ResponseEntity<List<Sales>> getAll() {
        try {
            List<Sales> sales = salesDao.findAll();
            System.out.println("Fetched Sales: " + sales.toString()); // Debug
            return new ResponseEntity<>(sales, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<Sales> getSale(Integer id) {
        try {
            Sales sale = salesDao.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Sale not found"));

            return new ResponseEntity<>(sale, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<Sales> addSale(Sales sale) {
        try {
            salesDao.save(sale);
            return new ResponseEntity<>(sale, HttpStatus.OK);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<Sales> updateSale(Sales sale) {
        try {
            Sales existingSale = salesDao.findById(sale.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Sale not found"));

            existingSale.setCustomerCode(sale.getCustomerCode());
            existingSale.setQuantity(sale.getQuantity());
            existingSale.setDate(sale.getDate());
            existingSale.setProductCode(sale.getProductCode());
            existingSale.setRevenue(sale.getRevenue());
            existingSale.setSoldBy(existingSale.getSoldBy());


            Sales updatedSale = salesDao.save(existingSale);
            return new ResponseEntity<>(updatedSale, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> deleteSale(Integer id) {
        try {
            Sales sale = salesDao.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Sale not found"));
            salesDao.deleteById(id);
            return new ResponseEntity<>(sale.getId() + " is deleted successfully!", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Cacheable(value = "sales", key = "#productCode + #customerCode + #soldBy + #date")
    public ResponseEntity<List<Sales>> searchSales(String productCode, String customerCode, String soldBy, Date date) {
        try {
            Specification<Sales> specification = Specification.where(
                    SalesSpecification.hasCustomerCode(customerCode)
                            .and(SalesSpecification.hasDate(date))
                            .and(SalesSpecification.hasProductCode(productCode))
                            .and(SalesSpecification.hasSoldBy(soldBy))
            );

            List<Sales> sales = salesDao.findAll(specification);
            return new ResponseEntity<>(sales, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while searching for sales", e);
        }
    }

}


