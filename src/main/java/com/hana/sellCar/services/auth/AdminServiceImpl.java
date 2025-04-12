package com.hana.sellCar.services.auth;

import com.hana.sellCar.entities.User;
import com.hana.sellCar.enums.UserRole;
import com.hana.sellCar.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class AdminServiceImpl {

    private final UserRepository userRepository;

    public AdminServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void initAdmin() {
        createAdmin();
    }

    private void createAdmin() {
        if (userRepository.findByUserRole(UserRole.ADMIN).isEmpty()) {
            User admin = new User();
            admin.setEmail("admin@test.com");
            admin.setName("ADMIN");
            admin.setUserRole(UserRole.ADMIN); // don't forget this line!
            admin.setPassword(new BCryptPasswordEncoder().encode("admin"));
            userRepository.save(admin);
            System.out.println("ADMIN created successfully!!");
        } else {
            System.out.println("ADMIN already exists!!");
        }
    }
}
