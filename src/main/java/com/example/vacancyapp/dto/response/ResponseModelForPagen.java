package com.example.vacancyapp.dto.response;

import com.example.vacancyapp.dto.request.DataForPagination;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseModelForPagen <T>{
    private DataForPagination<T> result;
    private String status;
    private Boolean error;
    private int code;
}
