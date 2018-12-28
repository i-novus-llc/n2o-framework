package net.n2oapp.criteria.filters.rule;

import net.n2oapp.criteria.filters.Filter;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.criteria.filters.rule.base.Rule;

import java.util.ArrayList;
import java.util.List;

/**
 * Rule for merge in and overlap filters
 */
public class Overlap_Overlap implements Rule {

    @Override
    public Filter simplify(Filter left, Filter right) {
        Filter result = new Filter(FilterType.overlaps);
        List res = new ArrayList();
        List list1 = (List) left.getValue();
        List list2 = (List) right.getValue();
        for (Object o : list2) {
            if (list1.contains(o))
                res.add(o);
        }
        if (res.isEmpty()) {
            return null;
        }
        result.setValue(res);
        return result;
    }


    @Override
    public Pair<FilterType> getType() {
        return new Pair<>(FilterType.overlaps, FilterType.overlaps);
    }
}
