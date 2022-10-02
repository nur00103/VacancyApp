package com.example.vacancyapp.controller;

import com.example.vacancyapp.dto.request.UserRequest;
import com.example.vacancyapp.dto.response.ResponseModel;
import com.example.vacancyapp.dto.response.UserResponse;
import com.example.vacancyapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('USER','HR')")
    public ResponseModel<UserResponse> getUserById(@PathVariable @Valid Long userId){
        return userService.getUserById(userId);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('USER','HR')")
    public ResponseModel<UserResponse> updateUser(@PathVariable @Valid Long userId,@Valid @RequestBody UserRequest userRequest) throws MessagingException, UnsupportedEncodingException {
        return userService.updateUser(userId,userRequest);
    }
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('USER','HR')")
    public void deleteUser(@PathVariable @Valid Long userId){
        userService.deleteUser(userId);
    }

    @PostMapping("/save")
    public ResponseModel<UserResponse> save(@RequestBody @Valid UserRequest userRequest) throws MessagingException, UnsupportedEncodingException {
         return userService.save(userRequest);
    }
    @GetMapping("/confirmation/{token}")
    public ResponseModel<UserResponse> confirm(@PathVariable("token") String token) {
        return userService.confirmToken(token);
    }
    @PostMapping("/confirmation/{token}")
    public ResponseModel<UserResponse> changePassword(@PathVariable("token") String token,String password) {
        return userService.changePassword(token,password);
    }

    @PostMapping("/forgotPassword/{userId}")
    public String forgotPassword(@PathVariable Long userId) throws MessagingException, UnsupportedEncodingException {
        return userService.forgotPassword(userId);
    }

    @GetMapping("/users")
    @PreAuthorize("hasAnyAuthority('HR')")
    public ResponseModel<List<UserResponse>> getAllUsers(){
        return userService.getAllUsers();
    }



}
