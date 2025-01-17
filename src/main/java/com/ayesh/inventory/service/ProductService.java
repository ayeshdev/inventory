package com.ayesh.inventory.service;

import com.ayesh.inventory.dao.ProductDao;
import com.ayesh.inventory.entity.Product;
import com.ayesh.inventory.entity.Supplier;
import com.ayesh.inventory.specification.ProductSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductDao productDao;

    public ResponseEntity<List<Product>> getAll() {
        try {
            List<Product> products = productDao.findAll();
            System.out.println("Fetched Products: " + products.toString()); // Debug
            return new ResponseEntity<>(products, HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<Product> getProduct(Integer id) {
        try {
            Product product = productDao.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));

            return new ResponseEntity<>(product, HttpStatus.OK);
        }catch(IllegalArgumentException e) {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }catch(Exception e) {
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Product> updateProduct(Product product) {
        try {
            Product existingProduct = productDao.findById(product.getPid())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));

            existingProduct.setProductName(product.getProductName());
            existingProduct.setBrand(product.getBrand());
            existingProduct.setCostPrice(product.getCostPrice());
            existingProduct.setSellingPrice(product.getSellingPrice());
            existingProduct.setProductCode(product.getProductCode());

            Product updatedProduct = productDao.save(existingProduct);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        }catch(IllegalArgumentException e) {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }catch(Exception e) {
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> deleteProduct(Integer id) {
        try {
            Product product = productDao.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));
            productDao.deleteById(id);
            return new ResponseEntity<>(product.getProductName() + " is deleted successfully!", HttpStatus.OK);
        }catch(IllegalArgumentException e) {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }catch(Exception e) {
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Cacheable(value = "products", key = "#name + #code + #brand")
    public ResponseEntity<List<Product>> searchProducts(String name, String code, String brand) {
        try {
            Specification<Product> specification = Specification.where(
                    ProductSpecification.hasName(name)
                            .and(ProductSpecification.hasCode(code))
                            .and(ProductSpecification.hasBrand(brand))
            );

            List<Product> products = productDao.findAll(specification);
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while searching for products", e);
        }
    }

    public ResponseEntity<Product> addProduct(Product product) {
        try{
            productDao.save(product);
            return new ResponseEntity<>(product, HttpStatus.OK);
        }catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
