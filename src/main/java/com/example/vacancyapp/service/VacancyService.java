package com.example.vacancyapp.service;

import com.example.vacancyapp.dto.request.VacancyRequest;
import com.example.vacancyapp.dto.response.ResponseModel;
import com.example.vacancyapp.dto.response.ResponseModelForPagen;
import com.example.vacancyapp.dto.response.VacancyResponse;

import java.text.ParseException;
import java.util.List;

public interface VacancyService {
    ResponseModel<List<VacancyResponse>> getAllVacancies();

    ResponseModel<VacancyResponse> saveVacancy(VacancyRequest vacancyRequest) throws ParseException;

    ResponseModel<VacancyResponse> getVacancyById(Long vacancyId);

    ResponseModel<VacancyResponse> updateVacancy(Long vacancyId, VacancyRequest vacancyRequest) throws ParseException;

    void deleteVacancy(Long vacancyId);

    ResponseModelForPagen<List<VacancyResponse>> search(String name, String category, String address, Integer salary, String startDate, String endDate, Integer page, Integer size);
}
