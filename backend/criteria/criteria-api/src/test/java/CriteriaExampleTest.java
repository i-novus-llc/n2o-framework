import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.api.Criteria;
import net.n2oapp.criteria.api.Direction;
import net.n2oapp.criteria.api.FilteredCollectionPage;
import net.n2oapp.criteria.api.constructor.CriteriaConstructorResult;
import net.n2oapp.criteria.api.query.QueryPage;
import net.n2oapp.criteria.api.query.QueryPageDao;
import org.junit.Test;
import stub.TestPageDao;

import java.util.Arrays;
import java.util.List;

/**
 * @author iryabov
 * @since 02.12.2015
 */
public class CriteriaExampleTest {

    private List<Integer> data = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
    private QueryPageDao pageDao = new TestPageDao(data);

    @Test
    public void testPaging() {
        Criteria criteria = new Criteria();
        criteria.setPage(2);
        criteria.setSize(5);

        CollectionPage<Integer> page = new FilteredCollectionPage<>(data, criteria);
        assert page.getCollection().size() == 5;
        assert page.getCollection().iterator().next() == 6;
    }

    @Test
    public void testFiltering() {
        Criteria criteria = new Criteria();
        criteria.setPage(2);
        criteria.setSize(5);

        String selectTemplate = "select :select from table where :where order by :order";
        String countTemplate = "select count(*) from table where :where";
        CriteriaConstructorResult criteriaConstructor = new CriteriaConstructorResult()
                .addColumn("id")
                .addColumn("name")
                .addSearch("id > ?")
                .addSorting("name", Direction.ASC)
                .addParameter(5);
        QueryPage<Integer> page = new QueryPage<>(criteria, selectTemplate, countTemplate, criteriaConstructor);
        page.setPageDao(pageDao);
        page.init();//execute "select id, name from table where id > 5 order by name asc"

        assert page.getCount() == 10 : page.getCount();
        assert page.getCollection().iterator().next() == 6;
    }
}
