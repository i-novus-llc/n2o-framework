package net.n2oapp.criteria.filters.rule;

import net.n2oapp.criteria.filters.Filter;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.criteria.filters.rule.base.NotInListRule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: operehod
 * Date: 18.11.2014
 * Time: 17:27
 */
public class NotIn_NotIn extends NotInListRule {

    @Override
    @SuppressWarnings("unchecked")
    protected List getResultList(Filter left, Filter right) {
        Set res = new HashSet();
        for (Object o : (List) right.getValue()) {
            res.add(o);
        }
        for (Object o : (List) left.getValue()) {
            res.add(o);
        }
        return new ArrayList(res);
    }


    @Override
    public Pair<FilterType> getType() {
        return new Pair<>(FilterType.notIn, FilterType.notIn);
    }
}
