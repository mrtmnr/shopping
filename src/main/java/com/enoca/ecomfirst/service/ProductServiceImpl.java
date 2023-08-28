package com.enoca.ecomfirst.service;

import com.enoca.ecomfirst.DTOs.ProductsDTO;
import com.enoca.ecomfirst.entity.Product;
import com.enoca.ecomfirst.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository theProductRepository) {
        productRepository = theProductRepository;
    }

    @Override
    public List<ProductsDTO> findAll() {
        List<Product> products=productRepository.findAll();
        return products.stream().map(this::fromEntityToDTO).collect (Collectors.toList());
    }
    public ProductsDTO fromEntityToDTO(Product product){
        return new ProductsDTO(product.getId(),product.getPrice(),product.getStock(),product.getTitle());
    }
    public List<ProductsDTO> fromEntityListToDTO(List<Product>products){
        return products.stream().map(this::fromEntityToDTO).collect (Collectors.toList());
    }

    @Override
    public Product findById(int theId) {
        Optional<Product> result = productRepository.findById(theId);

        Product theProduct = null;

        if (result.isPresent()) {
            theProduct = result.get();
        }
        else {
            // we didn't find the Product
            throw new RuntimeException("Did not find employee id - " + theId);
        }

        return theProduct;
    }

    @Override
    public void save(Product theProduct) {
        productRepository.save(theProduct);
    }

    @Override
    public void deleteById(int theId) {
        productRepository.deleteById(theId);
    }





}
