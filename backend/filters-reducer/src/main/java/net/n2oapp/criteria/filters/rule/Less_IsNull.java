package net.n2oapp.criteria.filters.rule;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.criteria.filters.rule.base.AlwaysConflictRule;

/**
 * User: operehod
 * Date: 18.11.2014
 * Time: 17:27
 */
public class Less_IsNull extends AlwaysConflictRule {

    @Override
    public Pair<FilterType> getType() {
        return new Pair<>(FilterType.less, FilterType.isNull);
    }
}
