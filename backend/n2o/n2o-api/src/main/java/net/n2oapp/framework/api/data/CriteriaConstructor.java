package net.n2oapp.framework.api.data;

import net.n2oapp.framework.api.criteria.N2oPreparedCriteria;

/**
 * Конструктор java критериев
 */
public interface CriteriaConstructor {

    /**
     * Конструирование java критерия по классу и критерию N2O
     *
     * @param criteria      исходный критерий
     * @param criteriaClass класс критерия приложения
     * @return критерий, используемый в прилоежении
     */
    <T> T construct(N2oPreparedCriteria criteria, Class<T> criteriaClass);
}
