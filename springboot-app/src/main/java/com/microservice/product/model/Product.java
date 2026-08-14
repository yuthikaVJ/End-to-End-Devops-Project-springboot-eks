package com.microservice.product.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Plain data holder for a product. No JPA / database annotations needed
 * anymore since products are persisted to a text (JSON) file instead of
 * a database.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private int id;
    private String productName;
    private String description;
    private int forSale;
}
