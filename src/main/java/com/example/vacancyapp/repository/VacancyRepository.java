package com.example.vacancyapp.repository;

import com.example.vacancyapp.entity.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy,Long> {

   // public Vacancy findByName(String name);
   // public Vacancy findByCategory(String category);

}
