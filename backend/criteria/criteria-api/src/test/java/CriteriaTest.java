import net.n2oapp.criteria.api.Criteria;
import net.n2oapp.criteria.api.Direction;
import net.n2oapp.criteria.api.Sorting;
import org.junit.Test;

/**
 * @author iryabov
 * @since 01.12.2015
 */
public class CriteriaTest {

    @Test
    public void testSorting() {
        Criteria criteria = new Criteria();
        //check initial setting
        criteria.setSorting(new Sorting("test1", Direction.DESC));
        assert criteria.getSorting() != null;
        assert criteria.getSorting().getField().equals("test1");
        assert criteria.getSorting().getDirection().equals(Direction.DESC);
        assert criteria.getSortings().size() == 1;

        //check re-setting
        criteria.setSorting(new Sorting("test2", Direction.ASC));
        assert criteria.getSorting() != null;
        assert criteria.getSorting().getField().equals("test2");
        assert criteria.getSorting().getDirection().equals(Direction.ASC);
        assert criteria.getSortings().size() == 2;
        assert criteria.getSortings().get(1).getField().equals("test1");
        assert criteria.getSortings().get(1).getDirection().equals(Direction.DESC);
    }


}
