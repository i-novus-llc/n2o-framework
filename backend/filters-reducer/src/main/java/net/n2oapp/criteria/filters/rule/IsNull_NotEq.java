package net.n2oapp.criteria.filters.rule;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.criteria.filters.rule.base.AlwaysSuccessRule;

/**
 * User: operehod
 * Date: 18.11.2014
 * Time: 17:27
 */
public class IsNull_NotEq extends AlwaysSuccessRule {

    public IsNull_NotEq() {
        super(FilterType.isNull);
    }

    @Override
    public Pair<FilterType> getType() {
        return new Pair<>(FilterType.isNull, FilterType.notEq);
    }
}
