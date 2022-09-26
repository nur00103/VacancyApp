package com.example.vacancyapp.service;

import com.example.vacancyapp.dto.request.LoginRequest;
import com.example.vacancyapp.dto.response.LoginResponse;
import com.example.vacancyapp.dto.response.ResponseModel;

public interface AuthService {
    ResponseModel<LoginResponse> login(LoginRequest loginRequest);
}
