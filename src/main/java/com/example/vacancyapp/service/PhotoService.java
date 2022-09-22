package com.example.vacancyapp.service;

import com.example.vacancyapp.dto.response.ResponseModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

public interface PhotoService {

    ResponseModel uploadPhoto(MultipartFile multipartFile);
}
