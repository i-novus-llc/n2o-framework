package net.n2oapp.tutorial.util;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.criteria.api.Criteria;

import java.util.List;

/**
 * Для записи критериев поиска по товарам
 */
@Getter
@Setter
public class ProductCriteria extends Criteria {

    private Long id;
    private String name;
    private Long price;
    private String description;
    private List<Long> categoryId;
    private Long minPrice;
    private Long maxPrice;
}
