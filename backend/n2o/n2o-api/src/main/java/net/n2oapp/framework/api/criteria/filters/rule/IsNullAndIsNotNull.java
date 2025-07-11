package net.n2oapp.framework.api.criteria.filters.rule;

import net.n2oapp.framework.api.criteria.filters.FilterTypeEnum;
import net.n2oapp.framework.api.criteria.filters.Pair;
import net.n2oapp.framework.api.criteria.filters.rule.base.AlwaysConflictRule;

/**
 * User: operehod
 * Date: 18.11.2014
 * Time: 17:27
 */
public class IsNullAndIsNotNull extends AlwaysConflictRule {

    @Override
    public Pair<FilterTypeEnum> getType() {
        return new Pair<>(FilterTypeEnum.IS_NOT_NULL, FilterTypeEnum.IS_NULL);
    }
}
