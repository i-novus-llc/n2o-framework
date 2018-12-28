package net.n2oapp.criteria.filters.rule;

import net.n2oapp.criteria.filters.Filter;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.criteria.filters.rule.base.NotInListRule;

import java.util.ArrayList;
import java.util.List;

/**
 * User: operehod
 * Date: 18.11.2014
 * Time: 17:27
 */
public class NotEq_NotIn extends NotInListRule {

    @Override
    @SuppressWarnings("unchecked")
    protected List getResultList(Filter left, Filter right) {
        if (right.getType().equals(FilterType.notEq))
            return getResultList(right, left);
        List res = new ArrayList();
        List list = (List) right.getValue();
        res.addAll(list);
        if (!list.contains(left.getValue()))
            res.add(left.getValue());
        return res;
    }


    @Override
    public Pair<FilterType> getType() {
        return new Pair<>(FilterType.notEq, FilterType.notIn);
    }
}
