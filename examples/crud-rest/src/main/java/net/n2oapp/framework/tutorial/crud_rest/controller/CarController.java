package net.n2oapp.framework.tutorial.crud_rest.controller;

import net.n2oapp.framework.tutorial.crud_rest.model.Car;
import net.n2oapp.framework.tutorial.crud_rest.model.CarCriteria;
import net.n2oapp.framework.tutorial.crud_rest.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/data/cars")
public class CarController {

    @Autowired
    private CarService carService;

    @PostMapping
    public Car create(@RequestBody Car car) {
        return carService.create(car);
    }

    @GetMapping
    public Page<Car> findAll(CarCriteria criteria) {
        return carService.findAll(criteria);
    }

    @GetMapping("/{id}")
    public Car findById(@PathVariable Long id) {
        return carService.findById(id);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody Car car) {
        carService.update(id, car);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        carService.delete(id);
    }
}