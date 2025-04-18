package com.hana.sellCar.repositories;

import com.hana.sellCar.entities.Car;
import com.hana.sellCar.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car,Long> {
    List<Car> findByUser(Optional<User> userId);
}
