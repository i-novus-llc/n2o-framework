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
public class InAndMore extends InListRule {
    @Override
    @SuppressWarnings("unchecked")
    public Filter simplify(Filter left, Filter right) {
        if (right.getType().equals(FilterTypeEnum.IN) && left.getType().equals(FilterTypeEnum.MORE))
            return simplify(right, left);
        else if (left.getType().equals(FilterTypeEnum.IN) && right.getType().equals(FilterTypeEnum.MORE)) {
            return super.simplify(left, right);
        }
        throw new IllegalArgumentException(
                String.format("Некорректные типы фильтров: `%s` и `%s`",
                        left.getType().getId(), right.getType().getId()));
    }

    @Override
    protected List getResultList(Filter left, Filter right) {
        List list = (List) left.getValue();
        Comparable bottom = (Comparable) right.getValue();
        List res = new ArrayList();
        for (Object o : list) {
            Comparable comparable = (Comparable) o;
            if (comparable.compareTo(bottom) > 0)
                res.add(comparable);
        }
        return res;
    }


    @Override
    public Pair<FilterTypeEnum> getType() {
        return new Pair<>(FilterTypeEnum.IN, FilterTypeEnum.MORE);
    }
}
