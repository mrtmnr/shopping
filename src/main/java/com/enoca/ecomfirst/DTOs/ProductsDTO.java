package com.enoca.ecomfirst.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductsDTO {
    private int id;

    private int price;

    private int stock;

    private String title;

}
