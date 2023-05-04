package org.parking.service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import org.parking.model.CarData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ParkingCamerasImitatorServiceImpl implements ParkingCamerasImitatorService {

	private static final int MIN_PROBABILITY = 1;
	private static final int MAX_PROBABILITY = 100;
	
	private Map<Long, Long> carsParkingPlaces = new HashMap<>();
	private Set<Long> parkingPlaces = new HashSet<>();
	private Map<Long, Boolean> carsAll = new HashMap<>();
	
	@Value("${app.carsID.min:10000000}")
	private int minCarID;
	
	@Value("${app.cars.amount:100}")
	private int numberOfCars;

	@Value("${app.parkingPlaceID.min:1}")
	private int minParkingPlaceID;

	@Value("${app.parking.amount:100}")
	private int numberOfParkingPlaces;
	
	@Value("${app.absent.probability:5}")
	private int probabilityAbsence;

	@Override
	public CarData nextCar() {
		long carID = getCarID();
		long parkingPlaceID = getParkingPlaceID(carID);
		log.debug("carID: {}, parkingPlaceID: {}", carID, parkingPlaceID);
		return new CarData(carID, parkingPlaceID);
	}

	private long getCarID() {
		long maxCarID = minCarID + numberOfCars;
		long carID = 0;
		boolean res = false;
		while(!res) {
			carID = generateLong(minCarID, maxCarID);
			res = carsAll.compute(carID, (id, isPresent) -> {
				if(isPresent == null) {
					return true;
				} else if(isPresent == false) {
					return false;
				}
				if(getRandomBoolean(probabilityAbsence)) {
					log.info("carId: {} left parking", id);
					return false;
				}
				return true;				
			});
		}
		return carID;
	}

	private long generateLong(int min, long max) {
		return getRandom().nextLong(min, max + 1);
	}
	
	private ThreadLocalRandom getRandom() {
		return ThreadLocalRandom.current();
	}

	private boolean getRandomBoolean(int probability) {
		return getRandom().nextInt(MIN_PROBABILITY, MAX_PROBABILITY + 1) <= probability;
	}

	private long getParkingPlaceID(long carID) {
		return carsParkingPlaces.computeIfAbsent(carID, k -> getParkingID());
	}

	private long getParkingID() {
		long maxParkingID = minParkingPlaceID + numberOfParkingPlaces;
		long parkingPlaceID;
		do {
			parkingPlaceID = generateLong(minParkingPlaceID, maxParkingID);
		} while(!parkingPlaces.add(parkingPlaceID));
		return parkingPlaceID;
	}

}
