package com.example.vacancyapp.service.impl;

import com.example.vacancyapp.dao.VacancyDao;
import com.example.vacancyapp.dto.request.DataForPagination;
import com.example.vacancyapp.dto.request.Pagination;
import com.example.vacancyapp.dto.request.VacancyRequest;
import com.example.vacancyapp.dto.response.ResponseModel;
import com.example.vacancyapp.dto.response.ResponseModelForPagen;
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

    private final VacancyDao vacancyDao;

    private final SkillServiceImpl skillServiceImpl;

    private int pageElementSize=10;

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

    @Override
    public ResponseModel<VacancyResponse> getVacancyById(Long vacancyId) {
        Vacancy vacancy=vacancyRepository.findById(vacancyId).orElseThrow(()->new MyException(ExceptionEnum.EMPTY_VACANCY));
        VacancyResponse vacancyResponse=convertToResponse(vacancy);
        return ResponseModel.<VacancyResponse>builder().result(vacancyResponse).code(ExceptionEnum.SUCCESS.getCode())
                .status(ExceptionEnum.SUCCESS.getMessage()).error(false).build();
    }

    @Override
    public ResponseModel<VacancyResponse> updateVacancy(Long vacancyId, VacancyRequest vacancyRequest) throws ParseException {
        Vacancy vacancy=vacancyRepository.findById(vacancyId).orElseThrow(()->new MyException(ExceptionEnum.EMPTY_VACANCY));
        if (vacancyRequest==null){
            throw new MyException(ExceptionEnum.BAD_REQUEST);
        }
        Vacancy updateData=convertToEntity(vacancyRequest);
        updateData.setId(vacancyId);
        Vacancy updatedVacancy=vacancyRepository.save(updateData);
        VacancyResponse vacancyResponse=convertToResponse(updatedVacancy);
        return ResponseModel.<VacancyResponse>builder().result(vacancyResponse).code(ExceptionEnum.SUCCESS.getCode())
                .status(ExceptionEnum.SUCCESS.getMessage()).error(false).build();
    }

    @Override
    public void deleteVacancy(Long vacancyId) {
        Vacancy vacancy=vacancyRepository.findById(vacancyId).orElseThrow(()->new MyException(ExceptionEnum.EMPTY_VACANCY));
        vacancy.setStatus(0);
        Vacancy deletedVacancy=vacancyRepository.save(vacancy);
        VacancyResponse vacancyResponse=convertToResponse(deletedVacancy);
    }

    @Override
    public ResponseModelForPagen<List<VacancyResponse>> search(String name, String category, String address, Integer salary, String startDate, String endDate, Integer page, Integer size) {
        int pageSize = size == null ? pageElementSize : size;
        int currentPage = page == null ? 0 : page;

        List<Vacancy> vacancyList = vacancyDao.list(name,category,address,salary,startDate,endDate,pageElementSize,currentPage);

        List<VacancyResponse> vacancyResponseList = vacancyList.stream().map(vacancy -> convertToResponse(vacancy)).collect(Collectors.toList());

        int totalElement = (int)vacancyRepository.count();

        int totalPage = (int)Math.ceil(totalElement/(double)pageSize);


        Pagination pagination = Pagination.builder()
                .currentPage(currentPage)
                .offset(vacancyResponseList.size())
                .totalElement(totalElement)
                .pageSize(totalPage)
                .build();

        DataForPagination<List<VacancyResponse>> pageData = DataForPagination.<List<VacancyResponse>>builder()
                .data(vacancyResponseList)
                .pagination(pagination)
                .build();

        return ResponseModelForPagen.<List<VacancyResponse>>builder()
                .result(pageData)
                .status(ExceptionEnum.SUCCESS.getMessage())
                .error(false)
                .code(ExceptionEnum.SUCCESS.getCode())
                .build();
    }

    public VacancyResponse convertToResponse(Vacancy vacancy){
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String startDate=formatter.format(vacancy.getStartDate());
        String endDate=formatter.format(vacancy.getEndDate());
        return VacancyResponse.builder().id(vacancy.getId()).address(vacancy.getAddress()).name(vacancy.getName())
                .category(vacancy.getCategory()).salary(vacancy.getSalary()).description(vacancy.getDescription())
                .startDate(startDate).endDate(endDate).user(vacancy.getUser())
                .status(vacancy.getStatus()).skills(vacancy.getSkills()).build();
    }

    public Vacancy convertToEntity(VacancyRequest vacancyRequest) throws ParseException {
        Vacancy vacancy=new Vacancy();
        vacancy.setName(vacancyRequest.getName());
        vacancy.setAddress(vacancyRequest.getAddress());
        vacancy.setCategory(vacancyRequest.getCategory());
        vacancy.setDescription(vacancyRequest.getDescription());
        vacancy.setSalary(vacancyRequest.getSalary());
        vacancy.setStartDate(vacancyRequest.getStartDate());
        vacancy.setEndDate(vacancyRequest.getEndDate());
        User user=userRepository.findById(vacancyRequest.getUserId()).get();
        vacancy.setUser(user);
        List<Skill> skillList=vacancyRequest.getSkills().stream()
                .map(skillRequest -> skillServiceImpl.convertToEntity(skillRequest)).collect(Collectors.toList());
        vacancy.setSkills(skillList);
        return vacancy;

    }
}
