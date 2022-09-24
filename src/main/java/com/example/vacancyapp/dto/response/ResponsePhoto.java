package com.example.vacancyapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponsePhoto {

    private PhotoResponse result;
    private Boolean error;
    private int code;
}
