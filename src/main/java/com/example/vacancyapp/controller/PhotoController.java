package com.example.vacancyapp.controller;

import com.example.vacancyapp.dto.response.ResponsePhoto;
import com.example.vacancyapp.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class PhotoController {

    private final PhotoService photoService;

    @PostMapping("/upload")
    public ResponsePhoto uploadPhoto(@RequestPart("file")MultipartFile multipartFile){
        return photoService.uploadPhoto(multipartFile);
    }
}
