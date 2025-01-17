package com.ayesh.inventory.controller;

import com.ayesh.inventory.entity.Supplier;
import com.ayesh.inventory.exception.SupplierNotFoundException;
import com.ayesh.inventory.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("supplier")
public class SuplierController {

    @Autowired
    SupplierService supplierService;

    @GetMapping("/test")
    public String test(){
        return "Hello World";
    }

    @GetMapping("/allSuppliers")
    public ResponseEntity<List<Supplier>> getAll() {
        return supplierService.getAllSuppliers();
    }

    @PostMapping("/addSupplier")
    public ResponseEntity<Supplier> addSupplier(@RequestBody Supplier supplier) {
        return supplierService.addSupplier(supplier);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Supplier> getSupplier(@PathVariable Integer id) {
        return supplierService.getSupplier(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Supplier> updateSupplier(@PathVariable Long id,@RequestBody Supplier supplier) {
        System.out.println(supplier.getId());
        if(supplier.getId() == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        try{
        return supplierService.updateSupplier(supplier);
        }catch (SupplierNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // Supplier not found
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); // Internal error
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSupplier(@PathVariable Integer id) {
        return supplierService.deleteSupplier(id);
    }
}
