package net.n2oapp.framework.tutorial.crud_java.specification;

import jakarta.persistence.criteria.*;
import net.n2oapp.framework.tutorial.crud_java.entity.CarEntity;
import net.n2oapp.framework.tutorial.crud_java.model.CarCriteria;
import org.springframework.data.jpa.domain.Specification;

public class CarSpecification implements Specification<CarEntity> {
    private final CarCriteria criteria;

    public CarSpecification(CarCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<CarEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate predicate = cb.conjunction();

        if (criteria.getMinPrice() != null) {
            predicate = cb.and(predicate,
                    cb.greaterThanOrEqualTo(root.get("price"), criteria.getMinPrice()));
        }

        if (criteria.getMaxPrice() != null) {
            predicate = cb.and(predicate,
                    cb.lessThanOrEqualTo(root.get("price"), criteria.getMaxPrice()));
        }

        return predicate;
    }
}