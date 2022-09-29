package com.example.vacancyapp.service.impl;

import com.example.vacancyapp.confirmation.ConfirmationToken;
import com.example.vacancyapp.confirmation.ConfirmationTokenRepo;
import com.example.vacancyapp.confirmation.ConfirmationTokenService;
import com.example.vacancyapp.confirmation.EmailSender;
import com.example.vacancyapp.dto.request.UserRequest;
import com.example.vacancyapp.dto.response.ResponseModel;
import com.example.vacancyapp.dto.response.UserResponse;
import com.example.vacancyapp.entity.Role;
import com.example.vacancyapp.entity.User;
import com.example.vacancyapp.enums.ExceptionEnum;
import com.example.vacancyapp.exception.MyException;
import com.example.vacancyapp.repository.RoleRepository;
import com.example.vacancyapp.repository.UserRepository;
import com.example.vacancyapp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public ResponseModel<UserResponse> save(UserRequest userRequest) {
       if (userRequest==null || userRequest.equals(null)){
           throw new MyException(ExceptionEnum.BAD_REQUEST);
       }
       if (userRepository.findByMail(userRequest.getEmail())!=null){
           throw new MyException(ExceptionEnum.MAIL);
       }
       log.info("Roles:{}",userRequest.getRole());
        String encodedPassword=passwordEncoder.encode(userRequest.getPassword());
        User user=convertToUser(userRequest);
        user.setPassword(encodedPassword);
        User savedUser=userRepository.save(user);
        UserResponse userResponse=convertToResponse(savedUser);
        return ResponseModel.<UserResponse>builder().result(userResponse).error(false)
                .code(ExceptionEnum.SUCCESS.getCode()).status(ExceptionEnum.SUCCESS.getMessage()).build();
    }

    @Override
    public ResponseModel<List<UserResponse>> getAllUsers() {
       List<User> userList=userRepository.findAll();
       List<UserResponse> userResponses=userList.stream().map(user -> convertToResponse(user)).collect(Collectors.toList());
       return ResponseModel.<List<UserResponse>>builder().result(userResponses).code(ExceptionEnum.SUCCESS.getCode())
               .status(ExceptionEnum.SUCCESS.getMessage()).error(false).build();
    }

    @Override
    public ResponseModel<UserResponse> getUserById(Long userId) {
        if (userId==null){
            throw new MyException(ExceptionEnum.INPUT);
        }
        User user=userRepository.findById(userId).orElseThrow(()-> new MyException(ExceptionEnum.USER_NOT_FOUND));
        UserResponse userResponse=convertToResponse(user);
        return ResponseModel.<UserResponse>builder().result(userResponse).error(false)
                .code(ExceptionEnum.SUCCESS.getCode()).status(ExceptionEnum.SUCCESS.getMessage()).build();
    }

    @Override
    public ResponseModel<UserResponse> updateUser(Long userId, UserRequest userRequest) {
        if (userId==null){
            throw new MyException(ExceptionEnum.INPUT);
        }
        if (userRequest==null){
            throw new MyException(ExceptionEnum.BAD_REQUEST);
        }
        User user=userRepository.findById(userId).orElseThrow(()->new MyException(ExceptionEnum.USER_NOT_FOUND));
        User data=convertToUser(userRequest);
        user.setName(data.getName());
        user.setSurname(data.getSurname());
        user.setMail(data.getMail());
        String encodedPassword=passwordEncoder.encode(data.getPassword());
        user.setPassword(encodedPassword);
//        user.setPassword(userRequest.getPassword());
        user.setPhoto(data.getPhoto());
        user.setRoles(data.getRoles());
        User updatedUser=userRepository.save(user);
        UserResponse userResponse=convertToResponse(updatedUser);
        return ResponseModel.<UserResponse>builder().result(userResponse).error(false)
                .code(ExceptionEnum.SUCCESS.getCode()).status(ExceptionEnum.SUCCESS.getMessage()).build();
    }

    @Override
    public void deleteUser(Long userId) {
        if (userId==null){
            throw new MyException(ExceptionEnum.INPUT);
        }
        User user=userRepository.findById(userId).orElseThrow(()->new MyException(ExceptionEnum.USER_NOT_FOUND));
        user.setStatus(0);
        userRepository.save(user);
    }



    public User convertToUser(UserRequest userRequest){
        User user=new User();
        user.setName(userRequest.getName());
        user.setSurname(userRequest.getSurname());
        user.setMail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        user.setPhoto(userRequest.getPhoto());
        Role role=new Role();
        List<Long> rolesId=userRequest.getRole().getId();
        List<Role> roles=roleRepository.findAllById(rolesId);
        if (roles.isEmpty()){
            throw new MyException(ExceptionEnum.ROLE);
        }else {
            log.info("Roles {}",roles);
            user.setRoles(roles);
            return user;
        }
    }

    public UserResponse convertToResponse(User user){
        return UserResponse.builder().id(user.getId()).name(user.getName()).surname(user.getSurname()).email(user.getMail())
                .status(user.getStatus()).photo(user.getPhoto()).role(user.getRoles()).build();
    }

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        UserDetails userDetails=userRepository.findByMail(mail);
        if (userDetails==null){
            throw new MyException(ExceptionEnum.USER_NOT_FOUND);
        }
        return userDetails;
    }
}
