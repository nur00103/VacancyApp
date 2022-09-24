package com.example.vacancyapp.service;

import com.example.vacancyapp.dto.request.SkillRequest;
import com.example.vacancyapp.dto.response.ResponseModel;
import com.example.vacancyapp.dto.response.RoleResponse;
import com.example.vacancyapp.dto.response.SkillResponse;

import java.util.List;

public interface SkillService {
    ResponseModel<List<SkillResponse>> getAllSkills();

    ResponseModel<SkillResponse> saveSkill(SkillRequest skillRequest);

    ResponseModel<List<RoleResponse>> getAllRoles();
}
