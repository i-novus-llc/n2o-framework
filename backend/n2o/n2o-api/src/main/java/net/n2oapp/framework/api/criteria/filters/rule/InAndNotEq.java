package net.n2oapp.framework.api.criteria.filters.rule;

import net.n2oapp.framework.api.criteria.filters.Filter;
import net.n2oapp.framework.api.criteria.filters.FilterTypeEnum;
import net.n2oapp.framework.api.criteria.filters.Pair;
import net.n2oapp.framework.api.criteria.filters.rule.base.InListRule;

import java.util.ArrayList;
import java.util.List;

/**
 * User: operehod
 * Date: 18.11.2014
 * Time: 17:27
 */
public class InAndNotEq extends InListRule {


    @Override
    protected List getResultList(Filter left, Filter right) {
        if (right.getType().equals(FilterTypeEnum.IN) && left.getType().equals(FilterTypeEnum.NOT_EQ))
            return getResultList(right, left);
        else if (left.getType().equals(FilterTypeEnum.IN) && right.getType().equals(FilterTypeEnum.NOT_EQ)) {
            List list = new ArrayList((List) left.getValue());
            if (((List) left.getValue()).contains(right.getValue())) {
                list.remove(right.getValue());
            }
            return list;
        }
        throw new IllegalArgumentException(
                String.format("Некорректные типы фильтров: `%s` и `%s`",
                        left.getType().getId(), right.getType().getId()));
    }


    @Override
    public Pair<FilterTypeEnum> getType() {
        return new Pair<>(FilterTypeEnum.IN, FilterTypeEnum.NOT_EQ);
    }
}
