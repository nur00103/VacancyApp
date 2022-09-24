package com.example.vacancyapp.controller;

import com.example.vacancyapp.dto.request.SkillRequest;
import com.example.vacancyapp.dto.response.ResponseModel;
import com.example.vacancyapp.dto.response.RoleResponse;
import com.example.vacancyapp.dto.response.SkillResponse;
import com.example.vacancyapp.repository.SkillRepository;
import com.example.vacancyapp.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;


    @GetMapping("/skills")
    public ResponseModel<List<SkillResponse>> getAllSkills(){
        return skillService.getAllSkills();
    }

    @GetMapping("/roles")
    public ResponseModel<List<RoleResponse>> getAllRoles(){
        return skillService.getAllRoles();
    }

    @PostMapping("/save")
    public ResponseModel<SkillResponse> saveSkill(@RequestBody @Valid SkillRequest skillRequest){
        return skillService.saveSkill(skillRequest);
    }



}
