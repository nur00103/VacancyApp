package com.example.vacancyapp.service.impl;

import com.example.vacancyapp.confirmation.ConfirmationToken;
import com.example.vacancyapp.confirmation.ConfirmationTokenRepo;
import com.example.vacancyapp.confirmation.ConfirmationTokenService;
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
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final ConfirmationTokenRepo confirmationTokenRepo;


    @Override
    @Transactional
    public ResponseModel<UserResponse> save(UserRequest userRequest) throws MessagingException, UnsupportedEncodingException {
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
        ConfirmationToken confirmationToken=new ConfirmationToken(user);
        confirmationTokenRepo.save(confirmationToken);
        confirmationTokenService.sendConfirmationMail(savedUser);
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
    @Transactional
    public ResponseModel<UserResponse> updateUser(Long userId, UserRequest userRequest) throws MessagingException, UnsupportedEncodingException {
        if (userId==null){
            throw new MyException(ExceptionEnum.INPUT);
        }
        if (userRequest==null){
            throw new MyException(ExceptionEnum.BAD_REQUEST);
        }
        User user=userRepository.findById(userId).orElseThrow(()->new MyException(ExceptionEnum.USER_NOT_FOUND));
        if (user.getMail()!=userRequest.getEmail()){
            User data=convertToUser(userRequest);
            user.setName(data.getName());
            user.setSurname(data.getSurname());
            String encodedPassword=passwordEncoder.encode(data.getPassword());
            user.setPassword(encodedPassword);
            user.setPhoto(data.getPhoto());
            user.setRoles(data.getRoles());
            user.setStatus(0);
            User updatedUser=userRepository.save(user);
            confirmationTokenService.sendConfirmationMail(updatedUser);
        }
        User data=convertToUser(userRequest);
        user.setName(data.getName());
        user.setSurname(data.getSurname());
        user.setMail(data.getMail());
        String encodedPassword=passwordEncoder.encode(data.getPassword());
        user.setPassword(encodedPassword);
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

    @Override
    @Transactional
    public ResponseModel<UserResponse> confirmToken(String token) {
            ConfirmationToken confirmToken = confirmationTokenService.getByToken(token);
            User user = userRepository.findByMail(confirmToken.getEmail());
            log.info("User by token {}",user);
            user.setStatus(1);
            userRepository.save(user);
            UserResponse userResponse=convertToResponse(user);
            confirmationTokenRepo.delete(confirmToken);
            return ResponseModel.<UserResponse>builder().result(userResponse).error(false)
                    .code(ExceptionEnum.SUCCESS.getCode()).status(ExceptionEnum.SUCCESS.getMessage()).build();

    }

    @Override
    @Transactional
    public ResponseModel<UserResponse> forgotPassword(String email) throws MessagingException, UnsupportedEncodingException {
        if (email==null){
            throw new MyException(ExceptionEnum.INPUT);
        }
        User user=userRepository.findByMail(email);
        ConfirmationToken confirmationToken=new ConfirmationToken(user);
        confirmationTokenRepo.save(confirmationToken);
        if (user==null){
            throw new MyException(ExceptionEnum.USER_NOT_FOUND);
        }
        confirmationTokenService.sendConfirmationMailForPassword(user);
        UserResponse userResponse=convertToResponse(user);
        return ResponseModel.<UserResponse>builder().result(userResponse).error(false)
                .code(ExceptionEnum.SUCCESS.getCode()).status(ExceptionEnum.SUCCESS.getMessage()+".Check your mail").build();
    }

    @Override
    public ResponseModel<UserResponse> changePassword(String token, String password) {
        ConfirmationToken confirmationToken=confirmationTokenService.getByToken(token);
        User user=userRepository.findByMail(confirmationToken.getEmail());
        user.setPassword(passwordEncoder.encode(password));
        User changedUser=userRepository.save(user);
        UserResponse userResponse=convertToResponse(changedUser);
        return ResponseModel.<UserResponse>builder().result(userResponse).error(false)
                .code(ExceptionEnum.SUCCESS.getCode()).status(ExceptionEnum.SUCCESS.getMessage()).build();
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
