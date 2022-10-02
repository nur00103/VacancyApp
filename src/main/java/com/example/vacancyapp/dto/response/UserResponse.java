package com.example.vacancyapp.dto.response;

import com.example.vacancyapp.dto.request.RoleRequest;
import com.example.vacancyapp.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String name;
    private String surname;
    private String email;
    private String photo;
    private List<Role> role;
    private Integer status;


}
