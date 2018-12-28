package net.n2oapp.criteria.api;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * User: iryabov
 * Date: 04.10.12
 * Time: 17:15
 */
public class FilteredCollectionPage<T> extends ComputationalCollectionPage<T> {
    private List<T> completeList;

    public FilteredCollectionPage(List<T> completeList, Criteria criteria) {
        super(criteria);
        this.completeList = completeList;
        init();
    }

    @Override
    public Collection<T> getCollectionInitial() {
        int fromIndex = criteria.getFirst();
        int toIndex = criteria.getSize() > 0 ? fromIndex + criteria.getSize() : getCount();
        int maxIndex = completeList.size();
        if (fromIndex >= maxIndex)
            return Collections.emptyList();
        if (toIndex >= maxIndex)
            toIndex = maxIndex;
        return completeList.subList(fromIndex, toIndex);
    }

    @Override
    public int getCountInitial() {
        return completeList.size();
    }

    @Override
    public Collection<Integer> getIdsInitial() {
        //throw new UnsupportedOperationException();
        return null;//из-за UnsupportedOperationException неудается через jackson прогнать в json
    }

    public List<T> getCompleteList() {
        return completeList;
    }
}
