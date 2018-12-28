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
public class InOrIsNull_InOrIsNull implements Rule {

    @Override
    public Filter simplify(Filter left, Filter right) {
        return toRestriction(getResultList(left, right));
    }


    private Filter toRestriction(List res) {
        if (res.size() == 0) {
            return new Filter(FilterType.isNull);
        } else if (res.size() == 1) {
            return new Filter(res.get(0), FilterType.eqOrIsNull);
        }
        return new Filter(res, FilterType.inOrIsNull);
    }

    @SuppressWarnings("unchecked")
    protected List getResultList(Filter left, Filter right) {
        List res = new ArrayList();
        List list1 = (List) left.getValue();
        List list2 = (List) right.getValue();
        for (Object o : list2) {
            if (list1.contains(o))
                res.add(o);
        }
        return res;
    }


    @Override
    public Pair<FilterType> getType() {
        return new Pair<>(FilterType.inOrIsNull, FilterType.inOrIsNull);
    }
}
