package com.example.vacancyapp.dto.request;

import com.example.vacancyapp.entity.Skill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VacancyRequest {

    private String name;
    private String category;
    private String address;
    private Double salary;
    private String startDate;
    private String endDate;
    private String description;
    private Long userId;
    private List<SkillRequest> skills;
}
