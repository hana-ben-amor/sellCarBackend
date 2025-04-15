package com.hana.sellCar.controllers;

import com.hana.sellCar.services.admin.AdminService;
import com.hana.sellCar.services.jwt.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @GetMapping("/cars")
    public ResponseEntity<?> getAll()
    {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.getAllCars());
    }

}
