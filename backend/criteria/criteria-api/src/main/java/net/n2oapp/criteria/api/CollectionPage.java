package net.n2oapp.criteria.api;


import java.io.Serializable;
import java.util.Collection;

public class CollectionPage<T> implements Serializable {
    protected Integer count;//total number of records
    protected Collection<T> collection;//current page with records
    protected Collection<Integer> ids;
    protected Criteria criteria;
    @Deprecated protected Class<T> entityClass;
    protected Object additionalInfo;

    @Deprecated
    protected CollectionPage(Criteria criteria, Class<T> entityClass) {
        this(criteria);
        this.entityClass = entityClass;
    }

    protected CollectionPage(Criteria criteria) {
        this.criteria = criteria;
        initCountFromCriteria();
    }

    private void initCountFromCriteria() {
        if (criteria.getCount() != null) {
            this.count = criteria.getCount();
        }
    }

    public CollectionPage() {

    }

    public CollectionPage(int count, Collection<T> collection, Criteria criteria) {
        this.criteria = criteria;
        init(count, collection);
    }

    @Deprecated
    public CollectionPage(int count, Collection<T> collection, Criteria criteria, Class<T> entityClass) {
        this(criteria, entityClass);
        init(count, collection);
    }

    /**
     * Getting count and collection
     */
    public void init() {
    }

    public void init(int count, Collection<T> collection) {
        this.count = count;
        this.collection = collection;
    }

    public Criteria getCriteria() {
        return criteria;
    }

    /**
     * Hack for cases when effectiveCriteria differs with originalCriteria (todo for boraldo)
     *
     * @param criteria
     */
    @Deprecated //criteria is required
    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
        initCountFromCriteria();
    }

    public int getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Collection<T> getCollection() {
        return collection;
    }

    public void setCollection(Collection<T> collection) {
        this.collection = collection;
    }

    @Deprecated
    public Class<T> getEntityClass() {
        return entityClass;
    }


    public int getPages() {
        if (getCriteria().getSize() == 0) {
            return 1;
        }
        Integer count = getCount();
        int pagesRatio = count / getCriteria().getSize();
        return count % criteria.getSize() == 0 ? pagesRatio : pagesRatio + 1;
    }

    public Collection<Integer> getIds() {
        return ids;
    }

    public void setIds(Collection<Integer> ids) {
        this.ids = ids;
    }

    public Object getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(Object additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}
