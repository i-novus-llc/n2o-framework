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
public class NotEq_NotEq extends NotInListRule {

    public NotEq_NotEq() {
    }

    @Override
    public Filter simplify(Filter left, Filter right) {
        return toRestriction(getResultList(left, right));
    }


    @SuppressWarnings("unchecked")
    protected List getResultList(Filter left, Filter right) {
        List res = new ArrayList();
        if (right.getValue().equals(left.getValue())) {
            res.add(left.getValue());
        } else {
            res.add(left.getValue());
            res.add(right.getValue());
        }
        return res;
    }

    private Filter toRestriction(List res) {
        if (res.size() == 0) {
            return null;
        } else if (res.size() == 1) {
            return new Filter(res.get(0), FilterType.notEq);
        }
        return new Filter(res, FilterType.notIn);
    }


    @Override
    public Pair<FilterType> getType() {
        return new Pair<>(FilterType.notEq, FilterType.notEq);
    }
}
