package net.n2oapp.framework.api.data;

import net.n2oapp.framework.api.criteria.N2oPreparedCriteria;

/**
 * Конструктор критериев
 */
public interface CriteriaConstructor {
    /**
     * Конструирование критерия по критерию фильтрации и экземпляру объекта
     *
     * @param criteria Критерий фильтрации
     * @param instance Экземпляр класса
     * @return Сконструированный критерий
     */
    Object construct(N2oPreparedCriteria criteria, Object instance);
}
