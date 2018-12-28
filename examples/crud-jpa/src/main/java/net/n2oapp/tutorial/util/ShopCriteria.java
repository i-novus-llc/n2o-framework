package net.n2oapp.tutorial.util;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.criteria.api.Criteria;

/**
 * Для записи критериев поиска по магазинам
 */
@Getter
@Setter
public class ShopCriteria extends Criteria {

    private Long id;
    private String name;
    private String address;
    private Long productId;
}
