package net.n2oapp.framework.engine.data;

import net.n2oapp.criteria.api.Criteria;
import net.n2oapp.framework.api.criteria.N2oPreparedCriteria;
import net.n2oapp.framework.api.data.CriteriaConstructor;

/**
 * Реализация конструктора критериев по умолчанию
 */
public class N2oCriteriaConstructor implements CriteriaConstructor {
    @Override
    public <T> T construct(N2oPreparedCriteria criteria, Class<T> criteriaClass) {
        T instance = null;
        try {
            instance = criteriaClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
        if (instance instanceof Criteria) {
            ((Criteria) instance).setSorting(criteria.getSorting());
            ((Criteria) instance).setSize(criteria.getSize());
            ((Criteria) instance).setPage(criteria.getPage());
            ((Criteria) instance).setCount(criteria.getCount());
        }
        return instance;
    }
}
