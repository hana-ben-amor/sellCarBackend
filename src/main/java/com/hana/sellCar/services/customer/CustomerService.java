package com.hana.sellCar.services.customer;

import com.hana.sellCar.dto.CarDTO;

import java.io.IOException;
import java.util.List;

public interface CustomerService {

    boolean createCar(CarDTO carDTO) throws IOException;
    List<CarDTO> getMyCars(Long userId);
}
