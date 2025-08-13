package net.n2oapp.framework.tutorial.crud_java.service;

import net.n2oapp.framework.tutorial.crud_java.dao.CarRepository;
import net.n2oapp.framework.tutorial.crud_java.entity.CarEntity;
import net.n2oapp.framework.tutorial.crud_java.model.Car;
import net.n2oapp.framework.tutorial.crud_java.model.CarCriteria;
import net.n2oapp.framework.tutorial.crud_java.specification.CarSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class CarService {
    @Autowired
    private CarRepository carRepository;

    public Car create(Car car) {
        CarEntity entity = toEntity(car, new CarEntity());
        carRepository.save(entity);
        car.setId(entity.getId());
        return car;
    }

    public Page<Car> findAll(CarCriteria criteria) {
        Pageable pageable = PageRequest.of(criteria.getPage(), criteria.getSize(), criteria.getOrder());
        Specification<CarEntity> spec = new CarSpecification(criteria);
        Page<CarEntity> carEntityPage = carRepository.findAll(spec, pageable);
        return carEntityPage.map(this::toModel);
    }

    public Car findById(Long id) {
        return toModel(carRepository.findById(id).orElseThrow(RuntimeException::new));
    }

    public void update(Car car) {
        CarEntity entity = carRepository.findById(car.getId()).orElseThrow(RuntimeException::new);
        carRepository.save(toEntity(car, entity));
    }

    public void delete(Long id) {
        CarEntity entity = carRepository.findById(id).orElseThrow(RuntimeException::new);
        carRepository.delete(entity);
    }

    private Car toModel(CarEntity entity) {
        if (entity == null) return null;
        Car car = new Car();
        car.setId(entity.getId());
        car.setName(entity.getName());
        car.setPrice(entity.getPrice());
        return car;
    }

    private static CarEntity toEntity(Car car, CarEntity entity) {
        entity.setName(car.getName());
        entity.setPrice(car.getPrice());
        return entity;
    }
}