package com.example.vacancyapp.service;

import com.example.vacancyapp.dto.response.ResponsePhoto;
import org.springframework.web.multipart.MultipartFile;

public interface PhotoService {

    ResponsePhoto uploadPhoto(MultipartFile multipartFile);
}
