package net.n2oapp.criteria.filters.rule.or;

import net.n2oapp.criteria.filters.Filter;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.criteria.filters.rule.base.Rule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: operehod
 * Date: 18.11.2014
 * Time: 17:27
 */
public class InOrIsNull_NotEq implements Rule {
    @Override
    @SuppressWarnings("unchecked")
    public Filter simplify(Filter left, Filter right) {
        if (right.getType().equals(FilterType.inOrIsNull) && left.getType().equals(FilterType.notEq))
            return simplify(right, left);
        else if (left.getType().equals(FilterType.inOrIsNull) && right.getType().equals(FilterType.notEq)) {
            if (((List) left.getValue()).contains(right.getValue())) {
                List list = new ArrayList((Collection) left.getValue());
                list.remove(right.getValue());
                if (list.isEmpty())
                    return new Filter(FilterType.isNull);
                else if (list.size() == 1)
                    return new Filter(list.get(0), FilterType.eqOrIsNull);
                return new Filter(list, FilterType.inOrIsNull);
            } else return left;
        }
        throw new RuntimeException("Incorrect restriction's type");
    }


    @Override
    public Pair<FilterType> getType() {
        return new Pair<>(FilterType.inOrIsNull, FilterType.notEq);
    }
}
