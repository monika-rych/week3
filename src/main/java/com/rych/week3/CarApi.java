package com.rych.week3;

import com.rych.week3.model.Car;
import com.rych.week3.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


@RestController
@RequestMapping(value = "/cars", produces =
        MediaType.APPLICATION_JSON_VALUE)
public class CarApi {


    private CarService carService;

    @Autowired
    private CarApi(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<Car>> getAllCars(@RequestParam("colour") Optional<String> colour) {
        List<Car> allCars = carService.getAllCars(colour);
        allCars.forEach(car -> car.add(linkTo(CarApi.class).slash(car.getId()).withSelfRel()));
        allCars.forEach(car -> car.add(linkTo(CarApi.class).slash(car.getId()).withRel("allColours")));
        Link link = linkTo(CarApi.class).withSelfRel();
        CollectionModel<Car> carCollectionModel = CollectionModel.of(allCars, link);

        return new ResponseEntity<>(carCollectionModel, HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Car>> getCar(@PathVariable Long id) {
        Link link = linkTo(CarApi.class).slash(id).withSelfRel();

        Optional<Car> first = carService.getCarBy(id);
        if (first.isPresent()) {
            EntityModel<Car> carResource = EntityModel.of(first.get(), link);
            return new ResponseEntity<>(carResource, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity addCar(@RequestBody Car car) {
        carService.addCar(car);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity updateCar(@RequestBody Car newCar) {
        boolean succes = carService.updateCar(newCar);
        if (succes) {

            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<Car> partialUpdateOfCar(@PathVariable Long id, @RequestBody HashMap<String, String> modifyCar) {
        Optional<Car> car = carService.partialUpdateOfCar(id, modifyCar);

        if (car.isPresent()) {
            return new ResponseEntity<>(car.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity deleteCar(@PathVariable Long id) {
        boolean succes = carService.deleteCar(id);
        if (succes) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
