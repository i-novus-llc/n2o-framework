package net.n2oapp.criteria.api.query;

import net.n2oapp.criteria.api.ComputationalCollectionPage;
import net.n2oapp.criteria.api.Criteria;
import net.n2oapp.criteria.api.constructor.CriteriaConstructorResult;

import java.util.Collection;

/**
 * User: iryabov
 * Date: 23.11.12
 * Time: 17:14
 */
public class QueryPage<T> extends ComputationalCollectionPage<T> {
    protected QueryPageDao pageDao;

    private String itemsQueryTemplate;
    private String countQueryTemplate;
    private String idsQueryTemplate;
    private String alias;
    private CriteriaConstructorResult criteriaResult;
    private Object rowMapper;

    public QueryPage(Criteria criteria, String countQueryTemplate, CriteriaConstructorResult criteriaResult) {
        super(criteria);
        this.countQueryTemplate = countQueryTemplate;
        this.criteriaResult = criteriaResult;
    }

    public QueryPage(Criteria criteria, String itemsQueryTemplate, String countQueryTemplate, CriteriaConstructorResult criteriaResult) {
        this(criteria, countQueryTemplate, criteriaResult);
        this.itemsQueryTemplate = itemsQueryTemplate;
    }

    public QueryPage(Criteria criteria, String itemsQueryTemplate, String countQueryTemplate, String idsQueryTemplate, CriteriaConstructorResult criteriaResult) {
        this(criteria, itemsQueryTemplate, countQueryTemplate, criteriaResult);
        this.idsQueryTemplate = idsQueryTemplate;
    }

    @Override
    public Collection<T> getCollectionInitial() {
        return pageDao.getList(getItemsQueryTemplate(), getCriteria(), getCriteriaResult(), getRowMapper());
    }

    @Override
    public int getCountInitial() {
        return pageDao.getCount(getCountQueryTemplate(), getCriteriaResult());
    }

    @Override
    public Collection<Integer> getIdsInitial() {
        throw new UnsupportedOperationException("can be implemented, if the return value is List<Integer>");
//        return pageDao.getIds(getIdsQueryTemplate(), getCriteria(), getCriteriaResult());
    }

    public String getItemsQueryTemplate() {
        return itemsQueryTemplate;
    }

    public String getCountQueryTemplate() {
        return countQueryTemplate;
    }

    public String getIdsQueryTemplate() {
        return idsQueryTemplate;
    }

    public Object getRowMapper() {
        return rowMapper;
    }

    public CriteriaConstructorResult getCriteriaResult() {
        return criteriaResult;
    }

    public void setCriteriaResult(CriteriaConstructorResult criteriaResult) {
        this.criteriaResult = criteriaResult;
    }

    public void setPageDao(QueryPageDao pageDao) {
        this.pageDao = pageDao;
    }

    public void setRowMapper(Object rowMapper) {
        this.rowMapper = rowMapper;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
