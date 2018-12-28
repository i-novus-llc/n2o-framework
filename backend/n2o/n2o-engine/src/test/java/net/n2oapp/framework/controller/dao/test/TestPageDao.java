package net.n2oapp.framework.controller.dao.test;

import net.n2oapp.criteria.api.Criteria;
import net.n2oapp.criteria.api.constructor.CriteriaConstructorResult;
import net.n2oapp.criteria.api.query.QueryPageDao;

import java.util.Collections;
import java.util.List;

/**
 * User: iryabov
 * Date: 21.02.13
 * Time: 11:41
 */
public class TestPageDao implements QueryPageDao {
    @Override
    public <T> List<T> getList(String itemsQueryTemplate, Criteria criteria, CriteriaConstructorResult criteriaResult) {
        return Collections.emptyList();
    }

    @Override
    public <T> List<T> getList(String itemsQueryTemplate, Criteria criteria, CriteriaConstructorResult criteriaResult,
                               Object rowMapper) {
        return Collections.emptyList();
    }

    @Override
    public int getCount(String countQueryTemplate, CriteriaConstructorResult criteriaResult) {
        return 0;
    }

    @Override
    public List<Integer> getIds(String idsQueryTemplate, Criteria criteria, CriteriaConstructorResult criteriaResult) {
        return Collections.emptyList();
    }
}
