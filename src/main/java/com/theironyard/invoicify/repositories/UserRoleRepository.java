package com.theironyard.invoicify.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.theironyard.invoicify.models.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long>{

}
