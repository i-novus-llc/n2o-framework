package net.n2oapp.criteria.filters.rule.or;

import net.n2oapp.criteria.filters.Filter;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.criteria.filters.rule.base.Rule;

import java.util.ArrayList;
import java.util.List;

/**
 * User: operehod
 * Date: 18.11.2014
 * Time: 17:27
 */
public class InOrIsNull_NotIn implements Rule {


    @Override
    public Filter simplify(Filter left, Filter right) {
        return toRestriction(getResultList(left, right));
    }

    @SuppressWarnings("unchecked")
    protected List getResultList(Filter left, Filter right) {
        if (right.getType().equals(FilterType.inOrIsNull))
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


    private Filter toRestriction(List res) {
        if (res.size() == 0) {
            return new Filter(FilterType.isNull);
        } else if (res.size() == 1) {
            return new Filter(res.get(0), FilterType.eqOrIsNull);
        }
        return new Filter(res, FilterType.inOrIsNull);
    }


    @Override
    public Pair<FilterType> getType() {
        return new Pair<>(FilterType.inOrIsNull, FilterType.notIn);
    }
}
