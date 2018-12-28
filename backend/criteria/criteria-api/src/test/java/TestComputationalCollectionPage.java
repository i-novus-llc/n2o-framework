import net.n2oapp.criteria.api.ComputationalCollectionPage;
import net.n2oapp.criteria.api.Criteria;

import java.util.Collection;

/**
 * @user iryabov
 * @since 04.08.2014
 */
public class TestComputationalCollectionPage extends ComputationalCollectionPage {

    private Collection collectionInitial;
    private int countInitial = -1;

    public TestComputationalCollectionPage(Criteria criteria, Collection collectionInitial, int countInitial) {
        super(criteria);
        this.countInitial = countInitial;
        this.collectionInitial = collectionInitial;
    }

    public TestComputationalCollectionPage(Criteria criteria, Collection collectionInitial) {
        super(criteria);
        this.collectionInitial = collectionInitial;
    }

    @Override
    public Collection getCollectionInitial() {
        return collectionInitial;
    }

    @Override
    public int getCountInitial() {
        if (countInitial == -1)
            throw new IllegalStateException("call count initial!");
        return countInitial;
    }

    @Override
    public Collection<Integer> getIdsInitial() {
        return null;
    }
}
