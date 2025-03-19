package net.n2oapp.criteria.api;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class CollectionPage<T> implements Serializable {
    private Integer count;
    private List<T> collection;
    private Criteria criteria;
    private Boolean hasNext;
    private Object additionalInfo;


    public CollectionPage() {

    }

    public CollectionPage(int count, List<T> collection, Criteria criteria) {
        this.count = count;
        this.collection = collection;
        this.criteria = criteria;
    }

    public CollectionPage(Boolean hasNext, List<T> collection, Criteria criteria) {
        this.hasNext = hasNext;
        this.collection = collection;
        this.criteria = criteria;
    }
}
