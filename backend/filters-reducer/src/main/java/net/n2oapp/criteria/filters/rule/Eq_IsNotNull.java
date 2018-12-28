package net.n2oapp.criteria.filters.rule;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.criteria.filters.rule.base.AlwaysSuccessRule;

/**
 * User: operehod
 * Date: 18.11.2014
 * Time: 17:27
 */
public class Eq_IsNotNull extends AlwaysSuccessRule {

    public Eq_IsNotNull() {
        super(FilterType.eq);
    }

    @Override
    public Pair<FilterType> getType() {
        return new Pair<>(FilterType.eq, FilterType.isNotNull);
    }
}
