package com.rr.jobtracker.service;

import com.rr.jobtracker.dto.LoginRequest;
import com.rr.jobtracker.dto.LoginResponse;
import com.rr.jobtracker.dto.RegisterRequest;
import com.rr.jobtracker.dto.UserResponse;
import com.rr.jobtracker.entity.User;
import com.rr.jobtracker.exception.EmailAlreadyExistsException;
import com.rr.jobtracker.exception.InvalidCredentialException;
import com.rr.jobtracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public UserResponse createUser(RegisterRequest request){

        String email = request.getEmail().trim().toLowerCase();

        if(userRepository.existsByEmail(email)){
            throw new EmailAlreadyExistsException(
                    "Email is already registered"
            );
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");

        User savedUser = userRepository.save(user);

        UserResponse response = new UserResponse();
        response.setId(savedUser.getId());
        response.setName(savedUser.getName());
        response.setEmail(savedUser.getEmail());
        response.setRole(savedUser.getRole());

        return response;
    }

    public LoginResponse login(LoginRequest request){

        String email = request.getEmail().trim().toLowerCase(Locale.ROOT);

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isEmpty()){
            throw new InvalidCredentialException("Invalid email or password");
        }

        User user = optionalUser.get();

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new InvalidCredentialException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getEmail());

        LoginResponse response = new LoginResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setToken(token);

        return response;
    }


    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();

        List <UserResponse> responses = new ArrayList<>();

        for(User user : users){
            UserResponse response = new UserResponse();
            response.setId(user.getId());
            response.setName(user.getName());
            response.setEmail(user.getEmail());
            response.setRole(user.getRole());

            responses.add(response);
        }
        return responses;

    }
}
