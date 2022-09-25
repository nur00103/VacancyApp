package com.example.vacancyapp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pagination {

    private int pageSize;
    private int totalElement;
    private int currentPage;
    private int offset;
}
