package com.hana.sellCar.controllers;

import com.hana.sellCar.dto.AuthRequest;
import com.hana.sellCar.dto.AuthResponse;
import com.hana.sellCar.dto.SignupRequest;
import com.hana.sellCar.dto.UserDTO;
import com.hana.sellCar.entities.User;
import com.hana.sellCar.repositories.UserRepository;
import com.hana.sellCar.services.auth.AuthService;
import com.hana.sellCar.services.jwt.UserService;
import com.hana.sellCar.utils.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthService authService, UserService userService, UserRepository userRepository, JWTUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        if (authService.hasUserWithEmail(signupRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        }

        UserDTO userDto = authService.signup(signupRequest);
        if (userDto == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getEmail(),
                            authRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect email or password!");
        }

        UserDetails userDetails = userService.userDetailsService().loadUserByUsername(authRequest.getEmail());
        Optional<User> optUser = userRepository.findFirstByEmail(authRequest.getEmail());

        if (optUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        String jwt = jwtUtil.generateToken(userDetails);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setUserId(optUser.get().getId());
        authResponse.setUserRole(optUser.get().getUserRole());

        return ResponseEntity.ok(authResponse);
    }
}
