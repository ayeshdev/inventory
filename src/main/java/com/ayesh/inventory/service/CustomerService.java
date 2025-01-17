package com.ayesh.inventory.service;

import com.ayesh.inventory.dao.CustomerDao;
import com.ayesh.inventory.entity.Customer;
import com.ayesh.inventory.entity.Product;
import com.ayesh.inventory.entity.Purchase;
import com.ayesh.inventory.specification.CustomerSpecification;
import com.ayesh.inventory.specification.ProductSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    CustomerDao customerDao;

    public ResponseEntity<List<Customer>> getAllCustomers() {
        try{
            List<Customer> customers = customerDao.findAll();
            return new ResponseEntity<>(customers, HttpStatus.OK);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public ResponseEntity<Customer> getCustomerById(Integer id) {
        try{
            Customer customer = customerDao.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

            return new ResponseEntity<>(customer, HttpStatus.OK);
        }catch(IllegalArgumentException e) {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }catch(Exception e) {
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Customer> addCustomer(Customer customer) {
        try {
            customerDao.save(customer);
            return new ResponseEntity<>(customer, HttpStatus.CREATED);
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public ResponseEntity<Customer> updateCustomer(Customer customer) {
        try {
            Customer existingCustomer = customerDao.findById(customer.getcId())
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

            existingCustomer.setFullName(customer.getFullName());
            existingCustomer.setCustomerCode(customer.getCustomerCode());
            existingCustomer.setLocation(customer.getLocation());
            existingCustomer.setPhoneNumber(customer.getPhoneNumber());

            Customer updatedCustomer = customerDao.save(existingCustomer);
            return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);

        }catch(IllegalArgumentException e) {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }catch(Exception e) {
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> deleteCustomer(Integer id) {
        try {
            Customer customer = customerDao.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
            customerDao.deleteById(id);
            return new ResponseEntity<>(customer.getFullName() + " is deleted successfully!", HttpStatus.OK);
        }catch(IllegalArgumentException e) {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }catch(Exception e) {
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Cacheable(value = "customers", key = "#customerName + #customerPhone")
    public ResponseEntity<List<Customer>> searchCustomer(String customerName, String customerPhone) {
        try {
            Specification<Customer> specification = Specification.where(
                    CustomerSpecification.hasCustomerName(customerName)
                            .and(CustomerSpecification.hasCustomerPhone(customerPhone))
            );

            List<Customer> customers = customerDao.findAll(specification);
            return new ResponseEntity<>(customers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while searching for customers", e);
        }
    }
}
