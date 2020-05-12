package com.rych.week3.service;

import com.rych.week3.model.Car;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarService {

    private List<Car> carList;

    public CarService() {
        this.carList = new ArrayList<>();
        carList.add(new Car(1L, "Audi", "GT", "Black"));
        carList.add(new Car(2L, "BMW", "3G", "Blue"));
        carList.add(new Car(3L, "Opel", "Astra", "Green"));
    }


    public Optional<Car> getCarBy(Long id) {
        Optional<Car> car = carList.stream().filter(c -> c.getId() == id).findFirst();
        return car;
    }

    public List<Car> getAllCars(Optional<String> colour) {
        if (colour.isPresent()) {
            List<Car> cars = carList.stream()
                    .filter(
                            car -> car.getColour()
                                    .toLowerCase()
                                    .equals(colour.get().toLowerCase()))
                    .collect(Collectors.toList());
            return cars;
        }
        return carList;
    }

    public void addCar(Car car) {
        carList.add(car);
    }

    public boolean updateCar(Car newCar) {
        Optional<Car> car = carList.stream().filter(c -> c.getId() == newCar.getId()).findFirst();
        if (car.isPresent()) {
            carList.remove(car.get());
            carList.add(newCar);
            return true;
        }
        return false;
    }

    public Optional<Car> partialUpdateOfCar(Long id, HashMap<String, String> modifyCar) {
        Optional<Car> car = carList.stream().filter(c -> c.getId() == id).findFirst();
        String colourPropertyName = "colour";
        String modelPropertyName = "model";
        String makePropertyName = "make";

        if (car.isPresent()) {
            if (modifyCar.containsKey(modelPropertyName)) {
                car.get().setModel(modifyCar.get(modelPropertyName));
            }
            if (modifyCar.containsKey(colourPropertyName)) {
                car.get().setColour(modifyCar.get(colourPropertyName));
            }
            if (modifyCar.containsKey(makePropertyName)) {
                car.get().setMake(modifyCar.get(makePropertyName));
            }
            return car;
        }
        return Optional.empty();
    }


    public boolean deleteCar(Long id) {
        Optional<Car> car = carList.stream().filter(c -> c.getId() == id).findFirst();
        if (car.isPresent()) {
            carList.remove(car.get());
            return true;
        }
        return false;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void start() {

    }
}
