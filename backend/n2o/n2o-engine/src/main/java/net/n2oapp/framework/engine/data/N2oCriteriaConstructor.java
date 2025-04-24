package net.n2oapp.framework.engine.data;

import net.n2oapp.criteria.api.Criteria;
import net.n2oapp.framework.api.criteria.N2oPreparedCriteria;
import net.n2oapp.framework.api.data.CriteriaConstructor;

/**
 * Реализация конструктора критериев по умолчанию
 */
public class N2oCriteriaConstructor implements CriteriaConstructor {

    private boolean pageStartsWith0;

    public N2oCriteriaConstructor(boolean pageStartsWith0) {
        this.pageStartsWith0 = pageStartsWith0;
    }

    @Override
    public Object construct(N2oPreparedCriteria criteria, Object instance) {
        if (instance instanceof Criteria criteriaInstance) {
            criteriaInstance.addSorting(criteria.getSorting());
            criteriaInstance.setSize(criteria.getSize());
            criteriaInstance.setPage(pageStartsWith0 ? criteria.getPage() -1 : criteria.getPage());
            criteriaInstance.setCount(criteria.getCount());
        }
        return instance;
    }
}
