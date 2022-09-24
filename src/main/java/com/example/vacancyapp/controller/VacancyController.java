package com.example.vacancyapp.controller;

import com.example.vacancyapp.dto.request.VacancyRequest;
import com.example.vacancyapp.dto.response.ResponseModel;
import com.example.vacancyapp.dto.response.VacancyResponse;
import com.example.vacancyapp.entity.Vacancy;
import com.example.vacancyapp.service.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/vacancy")
@RequiredArgsConstructor
public class VacancyController {

    private final VacancyService vacancyService;

    @GetMapping("/vacancies")
    public ResponseModel<List<VacancyResponse>> getAllVacancies(){
        return vacancyService.getAllVacancies();
    }
    @PostMapping("/save")
    public ResponseModel<VacancyResponse> saveVacancy(@RequestBody @Valid VacancyRequest vacancyRequest) throws ParseException {
        return vacancyService.saveVacancy(vacancyRequest);
    }
}
