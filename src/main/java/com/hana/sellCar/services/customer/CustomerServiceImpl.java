package com.hana.sellCar.services.customer;

import com.hana.sellCar.dto.CarDTO;
import com.hana.sellCar.entities.Car;
import com.hana.sellCar.entities.User;
import com.hana.sellCar.repositories.CarRepository;
import com.hana.sellCar.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService{

    @Autowired
    private  CarRepository carRepository;
    @Autowired
    private  UserRepository userRepository;


    public boolean createCar(CarDTO carDTO) throws IOException {
        if (carDTO.getUserId() == null) return false; // early exit

        Optional<User> optUser = userRepository.findById(carDTO.getUserId());
        if(optUser.isPresent()) {
            Car car = new Car();
            car.setBrand(carDTO.getBrand());
            car.setColor(carDTO.getColor());
            car.setDescription(carDTO.getDescription());
            car.setName(carDTO.getName());
            car.setYear(carDTO.getYear());
            car.setPrice(carDTO.getPrice());
            car.setSold(carDTO.isSold());
            car.setTransmission(carDTO.getTransmission());
            car.setType(carDTO.getType());
            car.setUser(optUser.get());

            if (carDTO.getImg() != null) {
                car.setImg(carDTO.getImg().getBytes());
            }

            carRepository.save(car);
            return true;
        }

        return false;
    }

    @Override
    public List<CarDTO> getMyCars(Long userId) {
        Optional<User> user=userRepository.findById(userId);
        return carRepository.findByUser(user).stream().map(Car::getCarDTO).collect(Collectors.toList());
    }

}
