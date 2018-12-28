package stub;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.api.Criteria;
import net.n2oapp.criteria.api.FilteredCollectionPage;
import net.n2oapp.criteria.api.constructor.CriteriaConstructorResult;
import net.n2oapp.criteria.api.query.QueryPageDao;

import java.util.List;

/**
 * @author iryabov
 * @since 02.12.2015
 */
@SuppressWarnings("unchecked")
public class TestPageDao implements QueryPageDao {

    private List data;

    public TestPageDao(List data) {
        this.data = data;
    }

    @Override
    public <T> List<T> getList(String itemsQueryTemplate, Criteria criteria, CriteriaConstructorResult criteriaResult) {
        return (List<T>) new FilteredCollectionPage(data, criteria).getCollection();
    }

    @Override
    public <T> List<T> getList(String itemsQueryTemplate, Criteria criteria, CriteriaConstructorResult criteriaResult, Object rowMapper) {
        return (List<T>) new FilteredCollectionPage(data, criteria).getCollection();
    }

    @Override
    public int getCount(String countQueryTemplate, CriteriaConstructorResult criteriaResult) {
        return data.size();
    }

    @Override
    public List<Integer> getIds(String idsQueryTemplate, Criteria criteria, CriteriaConstructorResult criteriaResult) {
        return null;
    }
}
