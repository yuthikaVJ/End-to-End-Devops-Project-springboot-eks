package com.microservice.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    // 0 (or omitted) when creating a new product -> the server assigns an id.
    private int id;
    private String productName;
    private String description;
    private int forSale;
}
