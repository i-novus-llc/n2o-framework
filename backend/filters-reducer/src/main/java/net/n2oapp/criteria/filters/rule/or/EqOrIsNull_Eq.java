package net.n2oapp.criteria.filters.rule.or;

import lombok.extern.slf4j.Slf4j;
import net.n2oapp.criteria.filters.Filter;
import net.n2oapp.criteria.filters.FilterTypeEnum;
import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.criteria.filters.rule.base.Rule;

/**
 * User: operehod
 * Date: 18.11.2014
 * Time: 17:27
 */
@Slf4j
public class EqOrIsNull_Eq implements Rule {

    @Override
    public Filter simplify(Filter left, Filter right) {
        Object leftValue = left.getValue();
        Object rightValue = right.getValue();
        if (leftValue.equals(rightValue)) return new Filter(rightValue);

        if (!leftValue.getClass().equals(rightValue.getClass()) && leftValue.toString().equals(rightValue.toString())) {
            log.info("Не получилось объединить фильтры <eqOrIsNull> и <eq> со значениями {}, имеющие разные типы {} и {}",
                    leftValue, leftValue.getClass().getSimpleName(), rightValue.getClass().getSimpleName());
        } else {
            log.info("Не получилось объединить фильтры <eqOrIsNull> и <eq> со значениями {} и {}",
                    leftValue, rightValue);
        }
        return null;
    }

    @Override
    public Pair<FilterTypeEnum> getType() {
        return new Pair<>(FilterTypeEnum.eqOrIsNull, FilterTypeEnum.eq);
    }
}
