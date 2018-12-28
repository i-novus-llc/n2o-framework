package net.n2oapp.criteria.api;


import java.util.Collection;

/**
 * User: boris.fanyuk
 * Date: 19.07.12
 * Time: 13:30
 */
public abstract class ComputationalCollectionPage<T> extends CollectionPage<T> {


    @Deprecated
    protected ComputationalCollectionPage(Criteria criteria, Class<T> entityClass) {
        super(criteria, entityClass);
    }

    protected ComputationalCollectionPage(Criteria criteria) {
        super(criteria);
    }

    public ComputationalCollectionPage(int count, Collection<T> ts, Criteria criteria) {
        super(count, ts, criteria);
    }

    @Deprecated
    public ComputationalCollectionPage(int count, Collection<T> ts, Criteria criteria, Class<T> entityClass) {
        super(count, ts, criteria, entityClass);
    }

    public abstract Collection<T> getCollectionInitial();

    @Override
    public Collection<T> getCollection() {
        if (collection == null)
            collection = getCollectionInitial();
        return collection;
    }

    public abstract int getCountInitial();

    @Override
    public int getCount() {
        if (count == null)
            count = getCountInitial();
        return count;
    }

    public abstract Collection<Integer> getIdsInitial();

    @Override
    public Collection<Integer> getIds() {
        if (ids == null)
            ids = getIdsInitial();
        return ids;
    }

    @Override
    public void init() {
        Collection<T> collection = getCollection();
        if ((collection.size() < criteria.getSize()) && ((collection.size() > 0) || (criteria.getPage() == 1))) {
            init(((criteria.getPage() - 1) * criteria.getSize()) + collection.size(), collection);
        } else {
            init(getCount(), collection);
        }
    }

    public boolean isPageProvided() {
        return collection != null || count != null;
    }
}
