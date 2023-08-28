package com.enoca.ecomfirst.service;

import com.enoca.ecomfirst.DTOs.EntryDTO;
import com.enoca.ecomfirst.DTOs.ProductsDTO;
import com.enoca.ecomfirst.entity.Entry;
import com.enoca.ecomfirst.entity.Product;
import com.enoca.ecomfirst.repository.EntryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EntryServiceImpl implements EntryService{

    private EntryRepository entryRepository;

    public EntryServiceImpl(EntryRepository entryRepository) {
        this.entryRepository = entryRepository;
    }

    @Override
    public List<Entry> findAll(){
        return entryRepository.findAll();
    }

    public EntryDTO fromEntryToDTO(Entry entry){
       Product product=entry.getProduct();
        return new EntryDTO(entry.getId(),entry.getQuantity(),new ProductsDTO(product.getId(), product.getPrice(),product.getStock(),product.getTitle()));
    }

   public List<EntryDTO> fromEntryListToDTO(List<Entry>entries){
        return entries.stream().map(this::fromEntryToDTO).collect (Collectors.toList());
    }


}
