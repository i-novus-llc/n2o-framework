package net.n2oapp.framework.api.criteria.filters.rule;

import net.n2oapp.framework.api.criteria.filters.Filter;
import net.n2oapp.framework.api.criteria.filters.FilterTypeEnum;
import net.n2oapp.framework.api.criteria.filters.Pair;
import net.n2oapp.framework.api.criteria.filters.rule.base.NotInListRule;

import java.util.ArrayList;
import java.util.List;

/**
 * User: operehod
 * Date: 18.11.2014
 * Time: 17:27
 */
public class NotEqAndNotIn extends NotInListRule {

    @Override
    @SuppressWarnings("unchecked")
    protected List getResultList(Filter left, Filter right) {
        if (right.getType().equals(FilterTypeEnum.NOT_EQ))
            return getResultList(right, left);
        List res = new ArrayList();
        List list = (List) right.getValue();
        res.addAll(list);
        if (!list.contains(left.getValue()))
            res.add(left.getValue());
        return res;
    }


    @Override
    public Pair<FilterTypeEnum> getType() {
        return new Pair<>(FilterTypeEnum.NOT_EQ, FilterTypeEnum.NOT_IN);
    }
}
