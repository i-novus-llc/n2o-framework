package net.n2oapp.criteria.filters.rule;

import net.n2oapp.criteria.filters.Filter;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.criteria.filters.rule.base.InListRule;

import java.util.ArrayList;
import java.util.List;

/**
 * User: operehod
 * Date: 18.11.2014
 * Time: 17:27
 */
public class In_NotEq extends InListRule {


    @Override
    protected List getResultList(Filter left, Filter right) {
        if (right.getType().equals(FilterType.in) && left.getType().equals(FilterType.notEq))
            return getResultList(right, left);
        else if (left.getType().equals(FilterType.in) && right.getType().equals(FilterType.notEq)) {
            List list = new ArrayList((List) left.getValue());
            if (((List) left.getValue()).contains(right.getValue())) {
                list.remove(right.getValue());
            }
            return list;
        }
        throw new RuntimeException("Incorrect restriction's type");
    }


    @Override
    public Pair<FilterType> getType() {
        return new Pair<>(FilterType.in, FilterType.notEq);
    }
}
