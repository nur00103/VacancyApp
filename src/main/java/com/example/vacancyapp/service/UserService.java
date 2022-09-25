package com.example.vacancyapp.service;

import com.example.vacancyapp.dto.request.UserRequest;
import com.example.vacancyapp.dto.response.ResponseModel;
import com.example.vacancyapp.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    ResponseModel<UserResponse> save(UserRequest userRequest);

    ResponseModel<List<UserResponse>> getAllUsers();

    ResponseModel<UserResponse> getUserById(Long userId);

    ResponseModel<UserResponse> updateUser(Long userId, UserRequest userRequest);

    void deleteUser(Long userId);

}
