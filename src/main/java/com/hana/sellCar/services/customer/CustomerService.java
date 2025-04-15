package com.hana.sellCar.services.customer;

import com.hana.sellCar.dto.CarDTO;

import java.io.IOException;

public interface CustomerService {

    boolean createCar(CarDTO carDTO) throws IOException;
}
