package com.enoca.ecomfirst.repository;

import com.enoca.ecomfirst.entity.Entry;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EntryRepository extends JpaRepository<Entry, Integer> {

}
