package net.n2oapp.criteria.filters.rule.base;

import net.n2oapp.criteria.filters.Filter;
import net.n2oapp.criteria.filters.FilterTypeEnum;

import java.util.List;

/**
 * User: operehod
 * Date: 18.11.2014
 * Time: 17:27
 */
public abstract class NotInListRule implements Rule {

    @Override
    public Filter simplify(Filter left, Filter right) {
        return toRestriction(getResultList(left, right));
    }

    protected abstract List getResultList(Filter left, Filter right);

    private Filter toRestriction(List res) {
        if (res.size() == 0) {
            return null;
        } else if (res.size() == 1) {
            return new Filter(res.get(0), FilterTypeEnum.notEq);
        }
        return new Filter(res, FilterTypeEnum.notIn);
    }

}
