package net.n2oapp.criteria.api.query;

import net.n2oapp.criteria.api.Criteria;
import net.n2oapp.criteria.api.constructor.CriteriaConstructorResult;

import java.util.List;

/**
 * User: iryabov
 * Date: 10.02.13
 * Time: 12:14
 */
public interface QueryPageDao {
    <T> List<T> getList(String itemsQueryTemplate, Criteria criteria,
                        CriteriaConstructorResult criteriaResult);

    <T> List<T> getList(String itemsQueryTemplate, Criteria criteria,
                        CriteriaConstructorResult criteriaResult, Object rowMapper);

    int getCount(String countQueryTemplate, CriteriaConstructorResult criteriaResult);

    List<Integer> getIds(String idsQueryTemplate, Criteria criteria, CriteriaConstructorResult criteriaResult);
}
