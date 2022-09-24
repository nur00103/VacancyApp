package com.example.vacancyapp.repository;

import com.example.vacancyapp.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository  extends JpaRepository<Skill,Long> {

    public Skill findByName(String name);
}
