package com.example.vacancyapp.exception;
import com.example.vacancyapp.enums.ExceptionEnum;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MyException extends RuntimeException{

    private final int code;
    private final String message;
    public MyException(ExceptionEnum errorCodeEnum){
        super(errorCodeEnum.getMessage());
        this.code= errorCodeEnum.getCode();
        this.message= errorCodeEnum.getMessage();
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }


}
