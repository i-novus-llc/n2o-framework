package net.n2oapp.criteria.filters.rule.or;

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
public class InOrIsNull_In extends InListRule {

    @Override
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
        return new Pair<>(FilterType.inOrIsNull, FilterType.in);
    }
}
