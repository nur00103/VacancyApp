package com.example.vacancyapp.dto.response;

import com.example.vacancyapp.dto.request.SkillRequest;
import com.example.vacancyapp.entity.Skill;
import com.example.vacancyapp.entity.User;
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
public class VacancyResponse {
    private Long id;
    private String name;
    private String category;
    private String address;
    private Double salary;
    private String startDate;
    private String endDate;
    private String description;
    private User user;
    private Integer status;
    private List<Skill> skills;
}
