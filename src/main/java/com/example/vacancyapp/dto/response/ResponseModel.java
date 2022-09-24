package com.example.vacancyapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseModel <T>{
    private T result;
    private Boolean error;
    private String status;
    private int code;
}
