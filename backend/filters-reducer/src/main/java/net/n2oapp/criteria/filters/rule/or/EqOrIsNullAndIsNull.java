package net.n2oapp.criteria.filters.rule.or;

import net.n2oapp.criteria.filters.FilterTypeEnum;
import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.criteria.filters.rule.base.AlwaysSuccessRule;

/**
 * User: operehod
 * Date: 18.11.2014
 * Time: 17:27
 */
public class EqOrIsNullAndIsNull extends AlwaysSuccessRule {


    public EqOrIsNullAndIsNull() {
        super(FilterTypeEnum.isNull);
    }

    @Override
    public Pair<FilterTypeEnum> getType() {
        return new Pair<>(FilterTypeEnum.eqOrIsNull, FilterTypeEnum.isNull);
    }
}
