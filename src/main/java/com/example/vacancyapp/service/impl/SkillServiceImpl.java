package com.example.vacancyapp.service.impl;

import com.example.vacancyapp.dto.request.SkillRequest;
import com.example.vacancyapp.dto.response.ResponseModel;
import com.example.vacancyapp.dto.response.RoleResponse;
import com.example.vacancyapp.dto.response.SkillResponse;
import com.example.vacancyapp.entity.Role;
import com.example.vacancyapp.entity.Skill;
import com.example.vacancyapp.enums.ExceptionEnum;
import com.example.vacancyapp.exception.MyException;
import com.example.vacancyapp.repository.RoleRepository;
import com.example.vacancyapp.repository.SkillRepository;
import com.example.vacancyapp.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;
    private final RoleRepository roleRepository;

    @Override
    public ResponseModel<List<SkillResponse>> getAllSkills() {
        List<Skill> skillList=skillRepository.findAll();
        if (skillList.isEmpty()){
            throw new MyException(ExceptionEnum.EMPTY_SKILL);
        }
        List<SkillResponse> skillResponseList=skillList.stream()
                .map(skill -> convertToResponse(skill)).collect(Collectors.toList());
        return ResponseModel.<List<SkillResponse>>builder().code(ExceptionEnum.SUCCESS.getCode())
                .result(skillResponseList).error(false).status(ExceptionEnum.SUCCESS.getMessage())
                .build();

    }

    @Override
    public ResponseModel<SkillResponse> saveSkill(SkillRequest skillRequest) {
        Skill skill=convertToEntity(skillRequest);
        Skill data=skillRepository.findByName(skillRequest.getName());
        SkillResponse skillResponse=null;
        if (skill==null){
            throw new MyException(ExceptionEnum.EMPTY_SKILL);
        }
        if (data!=null){
            skillResponse=convertToResponse(data);
        }
        else if ((data==null)){
            Skill savedSkill = skillRepository.save(skill);
            skillResponse = convertToResponse(savedSkill);
        }
        return ResponseModel.<SkillResponse>builder().code(ExceptionEnum.SUCCESS.getCode())
                .result(skillResponse).error(false).status(ExceptionEnum.SUCCESS.getMessage())
                .build();
    }

    @Override
    public ResponseModel<List<RoleResponse>> getAllRoles() {
        List<Role>roles=roleRepository.findAll();
        List<RoleResponse> roleResponseList=roles.stream().map(role -> convertRoleRes(role))
                .collect(Collectors.toList());
        return ResponseModel.<List<RoleResponse>>builder().code(ExceptionEnum.SUCCESS.getCode())
                .result(roleResponseList).error(false).status(ExceptionEnum.SUCCESS.getMessage())
                .build();
    }

    public SkillResponse convertToResponse(Skill skill){
        return SkillResponse.builder().id(skill.getId()).name(skill.getName()).build();
    }

    public Skill convertToEntity(SkillRequest skillRequest){
        Skill skill=new Skill();
        skill.setName(skillRequest.getName());
        return skill;
    }

    public RoleResponse convertRoleRes(Role role){
        return RoleResponse.builder().id(role.getId()).name(role.getName()).build();
    }
}
