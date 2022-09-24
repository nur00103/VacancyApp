package com.example.vacancyapp.service;

import com.example.vacancyapp.dto.request.VacancyRequest;
import com.example.vacancyapp.dto.response.ResponseModel;
import com.example.vacancyapp.dto.response.VacancyResponse;

import java.text.ParseException;
import java.util.List;

public interface VacancyService {
    ResponseModel<List<VacancyResponse>> getAllVacancies();

    ResponseModel<VacancyResponse> saveVacancy(VacancyRequest vacancyRequest) throws ParseException;
}
