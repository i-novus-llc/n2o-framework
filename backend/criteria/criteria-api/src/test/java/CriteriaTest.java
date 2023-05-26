import net.n2oapp.criteria.api.Criteria;
import net.n2oapp.criteria.api.SortingDirection;
import net.n2oapp.criteria.api.Sorting;

import org.junit.jupiter.api.Test;

/**
 * @author iryabov
 * @since 01.12.2015
 */
public class CriteriaTest {

    @Test
    void testSorting() {
        Criteria criteria = new Criteria();
        //check initial setting
        criteria.addSorting(new Sorting("test1", SortingDirection.DESC));
        assert criteria.getSorting() != null;
        assert criteria.getSorting().getField().equals("test1");
        assert criteria.getSorting().getDirection().equals(SortingDirection.DESC);
        assert criteria.getSortings().size() == 1;

        //check re-setting
        criteria.addSorting(new Sorting("test2", SortingDirection.ASC));
        assert criteria.getSorting() != null;
        assert criteria.getSorting().getField().equals("test2");
        assert criteria.getSorting().getDirection().equals(SortingDirection.ASC);
        assert criteria.getSortings().size() == 2;
        assert criteria.getSortings().get(1).getField().equals("test1");
        assert criteria.getSortings().get(1).getDirection().equals(SortingDirection.DESC);
    }
}
