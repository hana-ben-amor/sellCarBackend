package com.hana.sellCar.services.admin;

import com.hana.sellCar.entities.Car;
import com.hana.sellCar.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminSericeImpl implements AdminService {
    @Autowired
    private CarRepository carRepository;
    public List<Car> getAllCars()
    {
        return carRepository.findAll();
    }
}
