package com.example.vacancyapp.service;

import com.example.vacancyapp.dto.request.UserRequest;
import com.example.vacancyapp.dto.response.ResponseModel;
import com.example.vacancyapp.dto.response.UserResponse;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface UserService {

    ResponseModel<UserResponse> save(UserRequest userRequest) throws MessagingException, UnsupportedEncodingException;

    ResponseModel<List<UserResponse>> getAllUsers();

    ResponseModel<UserResponse> getUserById(Long userId);

    ResponseModel<UserResponse> updateUser(Long userId, UserRequest userRequest) throws MessagingException, UnsupportedEncodingException;

    void deleteUser(Long userId);

    ResponseModel<UserResponse> confirmToken(String token);

    ResponseModel<UserResponse> forgotPassword(String email) throws MessagingException, UnsupportedEncodingException;

    ResponseModel<UserResponse> changePassword(String token, String password);
}
