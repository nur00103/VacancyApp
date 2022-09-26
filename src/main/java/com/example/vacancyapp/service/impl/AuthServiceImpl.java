package com.example.vacancyapp.service.impl;

import com.example.vacancyapp.dto.request.LoginRequest;
import com.example.vacancyapp.dto.response.LoginResponse;
import com.example.vacancyapp.dto.response.ResponseModel;
import com.example.vacancyapp.entity.User;
import com.example.vacancyapp.enums.ExceptionEnum;
import com.example.vacancyapp.exception.MyException;
import com.example.vacancyapp.jwt.JwtService;
import com.example.vacancyapp.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

 private final AuthenticationManager authenticationManager;
 private final JwtService jwtService;
    @Override
    public ResponseModel<LoginResponse> login(LoginRequest loginRequest) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getMail(), loginRequest.getPassword());
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            User user= (User) authentication.getPrincipal();
            String token=jwtService.getToken(user);
            LoginResponse loginResponse=LoginResponse.builder().userId(user.getId()).accessToken(token).build();
            return ResponseModel.<LoginResponse>builder().result(loginResponse).error(false)
                    .status(ExceptionEnum.SUCCESS.getMessage()).code(ExceptionEnum.SUCCESS.getCode()).build();
        }catch (MyException e){
            throw new MyException(ExceptionEnum.USER_NOT_FOUND);
        }

    }
}
