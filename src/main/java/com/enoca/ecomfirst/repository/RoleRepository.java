package com.enoca.ecomfirst.repository;

import com.enoca.ecomfirst.Enums.EnumRole;
import com.enoca.ecomfirst.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(EnumRole name);


}
