package net.n2oapp.framework.tutorial.crud_rest.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@Data
public class CarCriteria {
    private Integer page;
    private Integer size;
    private Long minPrice;
    private Long maxPrice;
    private String sort;

    public Sort getOrder() {
        if (sort == null)
            return Sort.by("id").ascending();

        String[] sortParams = sort.split(",");
        return ("desc".equals(sortParams[1])) ?
                Sort.by(sortParams[0]).descending() :
                Sort.by(sortParams[0]).ascending();
    }
}