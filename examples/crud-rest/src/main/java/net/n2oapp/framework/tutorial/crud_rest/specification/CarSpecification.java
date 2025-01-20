package net.n2oapp.framework.tutorial.crud_rest.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import net.n2oapp.framework.tutorial.crud_rest.entity.CarEntity;
import net.n2oapp.framework.tutorial.crud_rest.model.CarCriteria;
import org.springframework.data.jpa.domain.Specification;

public class CarSpecification implements Specification<CarEntity> {
    private final CarCriteria criteria;

    public CarSpecification(CarCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<CarEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Predicate predicate = criteriaBuilder.conjunction();

        if (criteria.getMinPrice() != null) {
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.greaterThanOrEqualTo(root.get("price"), criteria.getMinPrice()));
        }

        if (criteria.getMaxPrice() != null) {
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.lessThanOrEqualTo(root.get("price"), criteria.getMaxPrice()));
        }

        return predicate;
    }
}