package com.example.vacancyapp.controller;

import com.example.vacancyapp.dto.request.UserRequest;
import com.example.vacancyapp.dto.response.ResponseModel;
import com.example.vacancyapp.dto.response.UserResponse;
import com.example.vacancyapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseModel<UserResponse> getUserById(@PathVariable @Valid Long userId){
        return userService.getUserById(userId);
    }

    @PutMapping("/{userId}")
    public ResponseModel<UserResponse> updateUser(@PathVariable @Valid Long userId,@Valid @RequestBody UserRequest userRequest){
        return userService.updateUser(userId,userRequest);
    }
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable @Valid Long userId){
        userService.deleteUser(userId);
    }

    @PostMapping("/save")
    public ResponseModel<UserResponse> save(@RequestBody @Valid UserRequest userRequest){
         return userService.save(userRequest);
    }

    @GetMapping("/users")
    public ResponseModel<List<UserResponse>> getAllUsers(){
        return userService.getAllUsers();
    }

//    @GetMapping(path = "confirm")
//    public String confirm(@RequestParam("token") String token) {
//        return userService.confirmToken(token);
//    }

}
