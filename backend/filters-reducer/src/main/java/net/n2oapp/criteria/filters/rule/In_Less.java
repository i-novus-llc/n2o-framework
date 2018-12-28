package net.n2oapp.criteria.filters.rule;

import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.criteria.filters.Filter;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.criteria.filters.rule.base.InListRule;

import java.util.ArrayList;
import java.util.List;

/**
 * User: operehod
 * Date: 18.11.2014
 * Time: 17:27
 */
public class In_Less extends InListRule {
    @Override
    @SuppressWarnings("unchecked")
    public Filter simplify(Filter left, Filter right) {
        if (right.getType().equals(FilterType.in) && left.getType().equals(FilterType.less))
            return simplify(right, left);
        else if (left.getType().equals(FilterType.in) && right.getType().equals(FilterType.less)) {
            return super.simplify(left, right);
        }
        throw new RuntimeException("Incorrect restriction's type");
    }

    @Override
    protected List getResultList(Filter left, Filter right) {
        List list = (List) left.getValue();
        Comparable top = (Comparable) right.getValue();
        List res = new ArrayList();
        for (Object o : list) {
            Comparable comparable = (Comparable) o;
            if (comparable.compareTo(top) < 0)
                res.add(comparable);
        }
        return res;
    }


    @Override
    public Pair<FilterType> getType() {
        return new Pair<>(FilterType.in, FilterType.less);
    }
}
