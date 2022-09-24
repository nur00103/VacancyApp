package com.example.vacancyapp.repository;

import com.example.vacancyapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    public String findByMail(String mail);
}
