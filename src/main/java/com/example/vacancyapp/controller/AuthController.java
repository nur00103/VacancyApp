package com.example.vacancyapp.controller;

import com.example.vacancyapp.dto.request.LoginRequest;
import com.example.vacancyapp.dto.response.LoginResponse;
import com.example.vacancyapp.dto.response.ResponseModel;
import com.example.vacancyapp.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseModel<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest){
      return authService.login(loginRequest);
    }
}
