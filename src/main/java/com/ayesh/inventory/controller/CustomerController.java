package com.ayesh.inventory.controller;

import com.ayesh.inventory.entity.Customer;
import com.ayesh.inventory.entity.Purchase;
import com.ayesh.inventory.exception.CustomerNotFoundException;
import com.ayesh.inventory.exception.PurchaseNotFoundException;
import com.ayesh.inventory.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @GetMapping("/getAllCustomers")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Integer id) {
        return customerService.getCustomerById(id);
    }

    @PostMapping("/addCustomer")
    public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer) {
        return customerService.addCustomer(customer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer) {
        if(customer.getcId() == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        try{
            return customerService.updateCustomer(customer);
        }catch (CustomerNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // Supplier not found
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); // Internal error
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePurchase(@PathVariable Integer id) {
        return customerService.deleteCustomer(id);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Customer>> searchProducts(
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String customerPhone) {
        return customerService.searchCustomer(customerName, customerPhone);
    }
}
