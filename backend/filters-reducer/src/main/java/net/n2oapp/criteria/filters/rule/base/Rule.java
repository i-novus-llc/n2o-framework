package net.n2oapp.criteria.filters.rule.base;

import net.n2oapp.criteria.filters.Filter;
import net.n2oapp.criteria.filters.FilterTypeEnum;
import net.n2oapp.criteria.filters.Pair;

/**
 * Правила объединения фильтров
 */
public interface Rule {

    /**
     * Объединяет 2 фильтра в один
     * @param left
     * @param right
     * @return объединение фильтров
     */
    Filter simplify(Filter left, Filter right);

    /**
     * Возвращает типы пары фильтров, для которой это правило работает
     * @return  типы фильтров
     */
    Pair<FilterTypeEnum> getType();

}
