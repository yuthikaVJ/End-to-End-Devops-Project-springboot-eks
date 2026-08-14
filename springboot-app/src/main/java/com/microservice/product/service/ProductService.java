package com.microservice.product.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.microservice.product.dto.ProductDTO;
import com.microservice.product.model.Product;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Stores products in a plain text (JSON) file on disk instead of a database.
 * All reads/writes go through this single file, guarded by a lock so
 * concurrent requests don't corrupt it.
 */
@Service
public class ProductService {

    // Where the "database" file lives. Configurable via
    // application.properties (product.storage.file=...).
    @Value("${product.storage.file:data/products.json}")
    private String storageFilePath;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    private final Object lock = new Object();

    @PostConstruct
    public void init() {
        File file = new File(storageFilePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        if (!file.exists()) {
            writeAll(new ArrayList<>());
        }
    }

    public ProductDTO saveProduct(ProductDTO productDTO) {
        synchronized (lock) {
            List<Product> products = readAll();

            int nextId = products.stream().mapToInt(Product::getId).max().orElse(0) + 1;

            Product product = new Product(
                    nextId,
                    productDTO.getProductName(),
                    productDTO.getDescription(),
                    productDTO.getForSale()
            );
            products.add(product);
            writeAll(products);

            return toDto(product);
        }
    }

    public List<ProductDTO> getAllProducts() {
        synchronized (lock) {
            List<Product> products = readAll();
            List<ProductDTO> result = new ArrayList<>();
            for (Product p : products) {
                result.add(toDto(p));
            }
            return result;
        }
    }

    public ProductDTO getProductById(int productId) {
        synchronized (lock) {
            Optional<Product> found = readAll().stream()
                    .filter(p -> p.getId() == productId)
                    .findFirst();
            if (found.isEmpty()) {
                throw new NoSuchElementException("No product found with id " + productId);
            }
            return toDto(found.get());
        }
    }

    public ProductDTO updateProduct(ProductDTO productDTO) {
        synchronized (lock) {
            List<Product> products = readAll();
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getId() == productDTO.getId()) {
                    Product updated = new Product(
                            productDTO.getId(),
                            productDTO.getProductName(),
                            productDTO.getDescription(),
                            productDTO.getForSale()
                    );
                    products.set(i, updated);
                    writeAll(products);
                    return toDto(updated);
                }
            }
            throw new NoSuchElementException("No product found with id " + productDTO.getId());
        }
    }

    public String deleteProduct(Integer productId) {
        synchronized (lock) {
            List<Product> products = readAll();
            boolean removed = products.removeIf(p -> p.getId() == productId);
            if (!removed) {
                throw new NoSuchElementException("No product found with id " + productId);
            }
            writeAll(products);
            return "Product deleted";
        }
    }

    // ---- file helpers ----

    private List<Product> readAll() {
        try {
            File file = new File(storageFilePath);
            if (!file.exists() || file.length() == 0) {
                return new ArrayList<>();
            }
            Product[] products = objectMapper.readValue(file, Product[].class);
            List<Product> list = new ArrayList<>();
            for (Product p : products) {
                list.add(p);
            }
            return list;
        } catch (IOException e) {
            throw new RuntimeException("Could not read product storage file: " + storageFilePath, e);
        }
    }

    private void writeAll(List<Product> products) {
        try {
            File file = new File(storageFilePath);
            objectMapper.writeValue(file, products);
        } catch (IOException e) {
            throw new RuntimeException("Could not write product storage file: " + storageFilePath, e);
        }
    }

    private ProductDTO toDto(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getProductName(),
                product.getDescription(),
                product.getForSale()
        );
    }
}
