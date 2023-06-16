import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.api.Criteria;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * @user iryabov
 * @since 04.08.2014
 */
public class CollectionPageTest {

    @Test
    void testCalcPages() {
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
    void testSizeZero() {
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
}
