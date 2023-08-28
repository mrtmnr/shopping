package com.enoca.ecomfirst.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntryDTO{
    private int id;
    private int quantity;
    private ProductsDTO product;


}
