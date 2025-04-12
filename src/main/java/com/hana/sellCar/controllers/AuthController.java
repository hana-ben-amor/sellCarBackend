package com.hana.sellCar.controllers;

import com.hana.sellCar.dto.SignupRequest;
import com.hana.sellCar.dto.UserDTO;
import com.hana.sellCar.services.auth.AuthService;
import com.hana.sellCar.services.auth.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> sigup(@RequestBody SignupRequest signupRequest)
    {
        if(authService.hasUserWithEmail(signupRequest.getEmail()))
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User already exists");

        UserDTO userDto=authService.signup(signupRequest);
        if(userDto == null)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }
}
