package com.example.vacancyapp.repository;

import com.example.vacancyapp.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role,Long> {

   @Override
   List<Role> findAllById(Iterable<Long> id);
}
