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
public class NotEqAndNotEq extends NotInListRule {

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
        if (res.isEmpty()) {
            return null;
        } else if (res.size() == 1) {
            return new Filter(res.get(0), FilterTypeEnum.NOT_EQ);
        }
        return new Filter(res, FilterTypeEnum.NOT_IN);
    }


    @Override
    public Pair<FilterTypeEnum> getType() {
        return new Pair<>(FilterTypeEnum.NOT_EQ, FilterTypeEnum.NOT_EQ);
    }
}
