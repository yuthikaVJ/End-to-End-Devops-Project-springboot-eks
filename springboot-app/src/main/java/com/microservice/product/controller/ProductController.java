package com.microservice.product.controller;

import com.microservice.product.dto.ProductDTO;
import com.microservice.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/addproduct")
    public ProductDTO saveProduct(@RequestBody ProductDTO productDTO) {
        return productService.saveProduct(productDTO);
    }

    @GetMapping("/getproducts")
    public List<ProductDTO> getProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable Integer productId) {
        try {
            return ResponseEntity.ok(productService.getProductById(productId));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/updateproduct")
    public ResponseEntity<?> updateProduct(@RequestBody ProductDTO productDTO) {
        try {
            return ResponseEntity.ok(productService.updateProduct(productDTO));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteproduct/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId) {
        try {
            return ResponseEntity.ok(productService.deleteProduct(productId));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
