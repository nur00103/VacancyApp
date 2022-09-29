package com.example.vacancyapp.controller;

import com.example.vacancyapp.dto.response.ResponseModel;
import com.example.vacancyapp.dto.response.UserResponse;
import com.example.vacancyapp.enums.ExceptionEnum;
import com.example.vacancyapp.exception.MyException;
import io.jsonwebtoken.JwtException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(MyException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseModel<UserResponse> expMy(MyException myException){
        return ResponseModel.<UserResponse>builder().status(myException.getMessage())
                .code(myException.getCode()).result(null).error(true).build();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public  ResponseModel<UserResponse> expReq(MethodArgumentTypeMismatchException e){
        return ResponseModel.<UserResponse>builder().status(ExceptionEnum.INPUT.getMessage())
                .code(ExceptionEnum.INPUT.getCode()).result(null).error(true).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseModel<UserResponse> exceptionInput(BindingResult bindingResult){
        final String[] error = {""};
        bindingResult.getFieldErrors()
                .stream()
                .forEach(fieldError -> {
                    error[0] = fieldError.getField()+" " + fieldError.getDefaultMessage() ;
                });
        return ResponseModel.<UserResponse>builder().status(error[0])
                .code(ExceptionEnum.BAD_REQUEST.getCode()).result(null).error(true).build();
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseModel<UserResponse> exceptionAllRunTime(RuntimeException e){
        return ResponseModel.<UserResponse>builder().status(e.getMessage())
                .code(404).result(null).error(true).build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseModel<UserResponse> exceptionAll(Exception e){
        return ResponseModel.<UserResponse>builder().status(e.getMessage())
                .code(404).result(null).error(true).build();
    }
}
