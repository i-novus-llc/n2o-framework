package net.n2oapp.criteria.filters.rule;

import net.n2oapp.criteria.filters.Filter;
import net.n2oapp.criteria.filters.FilterTypeEnum;
import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.criteria.filters.rule.base.InListRule;

import java.util.ArrayList;
import java.util.List;

/**
 * User: operehod
 * Date: 18.11.2014
 * Time: 17:27
 */
public class InAndNotIn extends InListRule {

    @Override
    @SuppressWarnings("unchecked")
    protected List getResultList(Filter left, Filter right) {
        if (right.getType().equals(FilterTypeEnum.IN))
            return getResultList(right, left);
        List listIn = (List) left.getValue();
        List listNotIn = (List) right.getValue();
        List res = new ArrayList(listIn);
        for (Object o : listIn) {
            if (listNotIn.contains(o))
                res.remove(o);
        }
        return res;
    }


    @Override
    public Pair<FilterTypeEnum> getType() {
        return new Pair<>(FilterTypeEnum.IN, FilterTypeEnum.NOT_IN);
    }
}
