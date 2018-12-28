import org.junit.Test;
import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.api.Criteria;
import net.n2oapp.criteria.api.FilteredCollectionPage;

import java.util.Arrays;

/**
 * @user iryabov
 * @since 04.08.2014
 */
public class CollectionPageTest {

    @Test
    public void testCalcPages() {
        Criteria criteria = new Criteria();
        CollectionPage page = new CollectionPage();
        page.setCriteria(criteria);

        criteria.setSize(3);
        page.init(3, Arrays.asList(1,2,3));
        assert page.getPages() == 1;

        criteria.setSize(4);
        page.init(3, Arrays.asList(1,2,3));
        assert page.getPages() == 1;

        criteria.setSize(1);
        page.init(3, Arrays.asList(1,2,3));
        assert page.getPages() == 3;

        criteria.setSize(2);
        page.init(3, Arrays.asList(1,2,3));
        assert page.getPages() == 2;
    }

    @Test
    public void testSizeZero() {
        Criteria criteria = new Criteria();
        CollectionPage page = new CollectionPage();
        page.setCriteria(criteria);

        criteria.setSize(0);
        page.init(3, Arrays.asList(1,2,3));
        assert page.getPages() == 1;

        criteria.setSize(0);
        page.init(30, Arrays.asList(1,2,3));
        assert page.getPages() == 1;
    }


    @Test
    public void testComputationalInit() {


        Criteria criteria = new Criteria();
        criteria.setSize(4);
        TestComputationalCollectionPage page = new TestComputationalCollectionPage(criteria, Arrays.asList(1,2,3));
        page.init();
        assert page.getCount() == 3;

        criteria = new Criteria();
        criteria.setSize(10);
        criteria.setPage(2);
        page = new TestComputationalCollectionPage(criteria, Arrays.asList(1,2,3,4,5));
        page.init();
        assert page.getCount() == 15;

        criteria = new Criteria();
        criteria.setSize(10);
        criteria.setPage(2);
        page = new TestComputationalCollectionPage(criteria, Arrays.asList(1,2,3,4,5,6,7,8,9,0), 100);
        page.init();
        assert page.getCount() == 100;


//        todo неплохой был бы кейс
//        criteria = new Criteria();
//        criteria.setSize(1);
//        page = new TestComputationalCollectionPage(criteria, Arrays.asList(1));
//        page.init();
//        assert page.getCount() == 1;
    }

    @Test
    public void testInitCountFromCriteria() {
        //check that count from criteria use
        Criteria criteria = new Criteria();
        criteria.setPage(2);
        criteria.setSize(2);
        criteria.setCount(100);
        TestComputationalCollectionPage page = new TestComputationalCollectionPage(criteria, Arrays.asList(1,2));
        page.init();
        assert page.getCount() == 100;

        //check that count recalc
        criteria = new Criteria();
        criteria.setPage(2);
        criteria.setSize(3);
        criteria.setCount(100);
        page = new TestComputationalCollectionPage(criteria, Arrays.asList(1,2));
        page.init();
        assert page.getCount() == 3 + 2;

        criteria = new Criteria();
        criteria.setPage(2);
        criteria.setSize(2);
        criteria.setCount(100);
        page = new TestComputationalCollectionPage(criteria, Arrays.asList(1,2));
        page.init(10, Arrays.asList(1,2));
        assert page.getCount() == 10;

        criteria = new Criteria();
        criteria.setPage(2);
        criteria.setSize(2);
        criteria.setCount(100);
        page = new TestComputationalCollectionPage(criteria, Arrays.asList(1,2));
        page.setCount(10);
        page.init();
        assert page.getCount() == 10;
    }

    @Test
    public void testFilteredCollectionInitial() {
        Criteria criteria = new Criteria();
        criteria.setSize(2);
        FilteredCollectionPage page = new FilteredCollectionPage(Arrays.asList(1,2,3), criteria);
        assert page.getCount() == 3;
        assert page.getCollection().size() == 2;

        criteria.setSize(4);
        page = new FilteredCollectionPage(Arrays.asList(1,2,3), criteria);
        assert page.getCount() == 3;
        assert page.getCollection().size() == 3;

        criteria = new Criteria();
        criteria.setSize(3);
        page = new FilteredCollectionPage(Arrays.asList(1,2,3), criteria);
        assert page.getCount() == 3;
        assert page.getCollection().size() == 3;

        criteria = new Criteria();
        criteria.setSize(2);
        criteria.setPage(2);
        page = new FilteredCollectionPage(Arrays.asList(1,2,3), criteria);
        assert page.getCount() == 3;
        assert page.getCollection().size() == 1;

        criteria = new Criteria();
        criteria.setSize(2);
        criteria.setPage(2);
        page = new FilteredCollectionPage(Arrays.asList(1,2,3,4), criteria);
        assert page.getCount() == 4;
        assert page.getCollection().size() == 2;

        criteria = new Criteria();
        criteria.setSize(2);
        criteria.setPage(3);
        page = new FilteredCollectionPage(Arrays.asList(1,2,3,4,5), criteria);
        assert page.getCount() == 5;
        assert page.getCollection().size() == 1;

        criteria = new Criteria();
        criteria.setSize(2);
        criteria.setPage(3);
        page = new FilteredCollectionPage(Arrays.asList(1,2,3,4), criteria);
        assert page.getCount() == 4;
        assert page.getCollection().size() == 0;

        criteria = new Criteria();
        criteria.setSize(2);
        criteria.setPage(4);
        page = new FilteredCollectionPage(Arrays.asList(1,2,3,4,5), criteria);
        assert page.getCount() == 5;
        assert page.getCollection().size() == 0;

        criteria = new Criteria();
        criteria.setSize(2);
        criteria.setPage(10);
        page = new FilteredCollectionPage(Arrays.asList(1,2,3,4), criteria);
        assert page.getCount() == 4;
        assert page.getCollection().size() == 0;
    }

    @Test
    public void testFilteredCollectionInitialWithSizeZero() {
        Criteria criteria = new Criteria();
        criteria.setSize(0);
        FilteredCollectionPage page = new FilteredCollectionPage(Arrays.asList(1,2,3), criteria);
        assert page.getCollection().size() == 3;
    }
}
