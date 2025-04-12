package com.hana.sellCar.services.auth;

import com.hana.sellCar.dto.SignupRequest;
import com.hana.sellCar.dto.UserDTO;

public interface AuthService {
    Boolean hasUserWithEmail(String email);
    UserDTO signup(SignupRequest signupRequest);
}
