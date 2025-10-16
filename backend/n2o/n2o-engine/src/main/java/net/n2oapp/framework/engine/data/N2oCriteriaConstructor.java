package net.n2oapp.framework.engine.data;

import net.n2oapp.criteria.api.Criteria;
import net.n2oapp.framework.api.criteria.N2oPreparedCriteria;
import net.n2oapp.framework.api.data.CriteriaConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Реализация конструктора критериев по умолчанию
 */
@Component
public class N2oCriteriaConstructor implements CriteriaConstructor<Criteria> {

    private final boolean pageStartsWith0;

    public N2oCriteriaConstructor(@Value("${n2o.engine.pageStartsWith0:false}") boolean pageStartsWith0) {
        this.pageStartsWith0 = pageStartsWith0;
    }

    @Override
    public Class<Criteria> getCriteriaClass() {
        return Criteria.class;
    }

    @Override
    public Criteria construct(N2oPreparedCriteria criteria, Criteria instance) {
        instance.addSorting(criteria.getSorting());
        instance.setSize(criteria.getSize());
        instance.setPage(pageStartsWith0 ? criteria.getPage() - 1 : criteria.getPage());
        instance.setCount(criteria.getCount());
        return instance;
    }
}