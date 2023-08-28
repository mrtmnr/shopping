package com.enoca.ecomfirst.service;

import com.enoca.ecomfirst.DTOs.EntryDTO;
import com.enoca.ecomfirst.entity.Entry;

import java.util.List;

public interface EntryService {

     List<Entry>findAll();

     EntryDTO fromEntryToDTO(Entry entry);

     List<EntryDTO> fromEntryListToDTO(List<Entry>entries);

}
