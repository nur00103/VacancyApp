package com.example.vacancyapp.service.impl;

import com.example.vacancyapp.dto.request.VacancyRequest;
import com.example.vacancyapp.dto.response.ResponseModel;
import com.example.vacancyapp.dto.response.UserResponse;
import com.example.vacancyapp.dto.response.VacancyResponse;
import com.example.vacancyapp.entity.Skill;
import com.example.vacancyapp.entity.User;
import com.example.vacancyapp.entity.Vacancy;
import com.example.vacancyapp.enums.ExceptionEnum;
import com.example.vacancyapp.exception.MyException;
import com.example.vacancyapp.repository.UserRepository;
import com.example.vacancyapp.repository.VacancyRepository;
import com.example.vacancyapp.service.SkillService;
import com.example.vacancyapp.service.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VacancyServiceImpl implements VacancyService {

    private final VacancyRepository vacancyRepository;
    private final UserRepository userRepository;

    private final SkillServiceImpl skillServiceImpl;

    @Override
    public ResponseModel<List<VacancyResponse>> getAllVacancies() {
        List<Vacancy> vacancies=vacancyRepository.findAll();
        if (vacancies.isEmpty()){
            throw new MyException(ExceptionEnum.EMPTY_VACANCY);
        }
        List<VacancyResponse> vacancyResponses=vacancies.stream().map(vacancy -> convertToResponse(vacancy))
                .collect(Collectors.toList());
        return ResponseModel.<List<VacancyResponse>>builder().result(vacancyResponses).code(ExceptionEnum.SUCCESS.getCode())
                .status(ExceptionEnum.SUCCESS.getMessage()).error(false).build();
    }

    @Override
    public ResponseModel<VacancyResponse> saveVacancy(VacancyRequest vacancyRequest) throws ParseException {
       Vacancy vacancy=convertToEntity(vacancyRequest);
       vacancy.setStatus(1);
       if (vacancy==null){
           throw new MyException(ExceptionEnum.BAD_REQUEST);
       }
       Vacancy savedVacancy=vacancyRepository.save(vacancy);
       VacancyResponse vacancyResponse=convertToResponse(savedVacancy);
        return ResponseModel.<VacancyResponse>builder().result(vacancyResponse).code(ExceptionEnum.SUCCESS.getCode())
                .status(ExceptionEnum.SUCCESS.getMessage()).error(false).build();
    }

    public VacancyResponse convertToResponse(Vacancy vacancy){
        DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        String startDate=formatter.format(vacancy.getStartDate());
        String endDate=formatter.format(vacancy.getEndDate());
        return VacancyResponse.builder().id(vacancy.getId()).address(vacancy.getAddress()).name(vacancy.getName())
                .category(vacancy.getCategory()).salary(vacancy.getSalary()).description(vacancy.getDescription())
                .startDate(startDate).endDate(endDate).user(vacancy.getUser())
                .status(vacancy.getStatus()).skills(vacancy.getSkills()).build();
    }

    public Vacancy convertToEntity(VacancyRequest vacancyRequest) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        Date startDate=formatter.parse(vacancyRequest.getStartDate());
        Date endDate=formatter.parse(vacancyRequest.getEndDate());
        Vacancy vacancy=new Vacancy();
        vacancy.setName(vacancyRequest.getName());
        vacancy.setAddress(vacancyRequest.getAddress());
        vacancy.setCategory(vacancyRequest.getCategory());
        vacancy.setDescription(vacancyRequest.getDescription());
        vacancy.setSalary(vacancyRequest.getSalary());
        vacancy.setStartDate(startDate);
        vacancy.setEndDate(endDate);
        User user=userRepository.findById(vacancyRequest.getUserId()).get();
        vacancy.setUser(user);
        List<Skill> skillList=vacancyRequest.getSkills().stream()
                .map(skillRequest -> skillServiceImpl.convertToEntity(skillRequest)).collect(Collectors.toList());
        vacancy.setSkills(skillList);
        return vacancy;

    }
}
