package net.n2oapp.framework.api.data;

import net.n2oapp.framework.api.criteria.N2oPreparedCriteria;

/**
 * Конструктор критериев
 */
public interface CriteriaConstructor<T> {

    /**
     * Поддерживаемый класс критериев
     */
    Class<T> getCriteriaClass();

    /**
     * Конструирование критерия по критерию фильтрации и экземпляру объекта
     *
     * @param criteria Критерий фильтрации
     * @param instance Экземпляр класса
     * @return Сконструированный критерий
     */
    T construct(N2oPreparedCriteria criteria, T instance);
}