package com.rr.jobtracker.controller;

import com.rr.jobtracker.dto.LoginRequest;
import com.rr.jobtracker.dto.LoginResponse;
import com.rr.jobtracker.dto.RegisterRequest;
import com.rr.jobtracker.dto.UserResponse;
import com.rr.jobtracker.entity.User;
import com.rr.jobtracker.service.UserService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService userService;
//    create user
    @PostMapping("/register")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody RegisterRequest request){
        UserResponse response = userService.createUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request){
        LoginResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }


    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }




}
