package com.example.vacancyapp.controller;

import com.example.vacancyapp.dto.request.VacancyRequest;
import com.example.vacancyapp.dto.response.ResponseModel;
import com.example.vacancyapp.dto.response.ResponseModelForPagen;
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
    @GetMapping("/{vacancyId}")
    public ResponseModel<VacancyResponse> getVacancyById(@PathVariable @Valid Long vacancyId){
        return vacancyService.getVacancyById(vacancyId);
    }
    @PutMapping("/{vacancyId}")
    public ResponseModel<VacancyResponse> updateVacancy(@PathVariable @Valid Long vacancyId,
                                                        @RequestBody @Valid VacancyRequest vacancyRequest) throws ParseException {
        return vacancyService.updateVacancy(vacancyId,vacancyRequest);
    }

    @DeleteMapping("/{vacancyId}")
    public void deleteVacancy(@PathVariable @Valid Long vacancyId){
        vacancyService.deleteVacancy(vacancyId);
    }

    @GetMapping("/search")
    public ResponseModelForPagen<List<VacancyResponse>> search(@RequestParam(value = "name", required = false) String name,
                                                               @RequestParam(value = "category", required = false) String category,
                                                               @RequestParam(value = "address", required = false) String address,
                                                               @RequestParam(value = "salary", required = false) Integer salary,
                                                               @RequestParam(value = "startDate", required = false) String startDate,
                                                               @RequestParam(value = "endDate", required = false) String endDate,
                                                               @RequestParam(value = "page", required = false) Integer page,
                                                               @RequestParam(value = "size", required = false) Integer size){
        return vacancyService.search(name,category,address,salary,startDate,endDate,page,size);
    }
}
