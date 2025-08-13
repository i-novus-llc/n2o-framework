package net.n2oapp.framework.tutorial.crud_java.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.criteria.api.Criteria;
import net.n2oapp.criteria.api.SortingDirection;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@Data
public class CarCriteria extends Criteria {
    private Long minPrice;
    private Long maxPrice;

    public Sort getOrder() {
        if (getSorting() == null)
            return Sort.by("id").ascending();

        return (getSorting().getDirection() == SortingDirection.DESC) ?
                Sort.by(getSorting().getField()).descending() :
                Sort.by(getSorting().getField()).ascending();
    }
}