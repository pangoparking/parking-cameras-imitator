package org.parking;

import java.util.function.Supplier;

import org.parking.model.CarData;
import org.parking.service.ParkingCamerasImitatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class ParkingCamerasImitatorApplication {
	
	@Autowired
	ParkingCamerasImitatorService imitator;

	public static void main(String[] args) {
		SpringApplication.run(ParkingCamerasImitatorApplication.class, args);
	}
	
	@Bean
	Supplier<CarData> carDataSupplier() {
		log.debug("creating an event for Middleware Broker");
		return imitator::nextCar;
	}

}
