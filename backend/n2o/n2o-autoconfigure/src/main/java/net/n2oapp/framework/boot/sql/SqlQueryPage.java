package net.n2oapp.framework.boot.sql;

import net.n2oapp.criteria.api.Criteria;
import net.n2oapp.criteria.api.constructor.CriteriaConstructorResult;
import net.n2oapp.criteria.api.query.QueryPage;
import net.n2oapp.criteria.dataset.DataSet;

/**
 * User: operehod
 * Date: 17.11.2014
 * Time: 11:22
 */
public class SqlQueryPage extends QueryPage<DataSet> {

    public SqlQueryPage(Criteria criteria, String itemsQueryTemplate, String countQueryTemplate, CriteriaConstructorResult criteriaResult) {
        super(criteria, itemsQueryTemplate, countQueryTemplate, null, criteriaResult);
    }

}
