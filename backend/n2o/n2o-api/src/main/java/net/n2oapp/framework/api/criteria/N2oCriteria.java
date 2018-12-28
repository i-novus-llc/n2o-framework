package net.n2oapp.framework.api.criteria;

import net.n2oapp.criteria.api.Criteria;
import net.n2oapp.framework.api.criteria.expression.*;
import net.n2oapp.framework.api.exception.N2oException;

import java.util.*;

/**
 * User: iryabov
 * Date: 27.08.13
 * Time: 16:40
 */
public class N2oCriteria extends Criteria {

    private Map<String, Object> filterDataSet;
    private List<FilterExpression> filterList;
    private List<DisplayExpression> displayList;
    private List<SortingExpression> sortingList;
    private List<JoinExpression> joinList;
    private Map<String, Object> attributes;

    public N2oCriteria() {

    }

    public N2oCriteria(Criteria criteria) {
        super(criteria);
    }

    public N2oCriteria(N2oCriteria criteria) {
        super(criteria);
        this.filterList = criteria.filterList;
        this.filterDataSet = criteria.filterDataSet;
        this.displayList = criteria.displayList;
        this.sortingList = criteria.sortingList;
        this.joinList = criteria.joinList;
    }

    public List<DisplayExpression> getDisplayList() {
        if (displayList == null)
            return Collections.emptyList();
        return displayList;
    }

    public List<SortingExpression> getSortingList() {
        if (sortingList == null)
            return Collections.emptyList();
        return sortingList;
    }

    public Map<String, String> getDisplayMapping() {
        Map<String, String> res = new HashMap<>();
        for (DisplayExpression display : getDisplayList()) {
            res.put(display.getFieldId(), display.getExpression());
        }
        return res;
    }

    public List<JoinExpression> getJoinList() {
        return joinList == null ? Collections.emptyList() : joinList;
    }

    public List<FilterExpression> getFilterList() {
        if (filterList == null)
            return Collections.emptyList();
        return filterList;
    }

    public Object getFilterValue(FilterExpression filter) {
        Object value = getFilterDataSet().get(filter.getFilterFieldId());
        if (value == null)
            throw new N2oException("filter-value for " + filter.getFilterFieldId() + " is null");
        return value;
    }

    public N2oCriteria addFilter(FilterExpression filter, Object value) {
        if (filterList == null)
            filterList = new ArrayList<>();
        filterList.add(filter);
        putFilterValue(filter.getFilterFieldId(), value);
        return this;
    }

    public Map<String, Object> getFilterDataSet() {
        if (filterDataSet == null)
            return Collections.emptyMap();
        return filterDataSet;
    }

    public N2oCriteria putFilterValue(String fieldId, Object value) {
        if (filterDataSet == null)
            filterDataSet = new HashMap<>();
        filterDataSet.put(fieldId, value);
        return this;
    }

    public N2oCriteria addDisplay(DisplayExpression display) {
        if (displayList == null)
            displayList = new ArrayList<>();
        displayList.add(display);
        return this;
    }


    public N2oCriteria addSorting(SortingExpression sorting) {
        if (sortingList == null)
            sortingList = new ArrayList<>();
        sortingList.add(sorting);
        return this;
    }

    public N2oCriteria addJoin(JoinExpression join) {
        if (joinList == null) {
            joinList = new ArrayList<>();
        }
        joinList.add(join);
        return this;
    }

    public N2oCriteria setJoins(Collection<JoinExpression> joins) {
        if (joinList == null) {
            joinList = new ArrayList<>(joins);
        } else {
            joinList.addAll(joins);
        }
        return this;
    }

    public N2oCriteria putFilterValues(Map<String, Object> values) {
        for (String key : values.keySet()) {
            if (filterDataSet == null || !filterDataSet.containsKey(key))
                putFilterValue(key, values.get(key));
        }
        return this;
    }

    public boolean byId() {
        for (FilterExpression filter : getFilterList()) {
            if (filter.getFilterFieldId().equals("id"))
                return true;
        }
        return false;
    }


    public void iterateFilters(FilterVisitor visitor) {
        for (FilterExpression filter : getFilterList()) {
            visitor.visit(filter, getFilterValue(filter));
        }
    }

    public void removeDisplay(String fieldId) {
        for (DisplayExpression displayExpression : new ArrayList<>(getDisplayList())) {
            if (fieldId.equals(displayExpression.getFieldId())) {
                getDisplayList().remove(new DisplayExpression(fieldId, null, null));
            }
        }
    }

    public Map<String, Object> getAttributes() {
        if (attributes == null)
            return Collections.emptyMap();
        return Collections.unmodifiableMap(attributes);
    }

    public Object getAttribute(String attribute) {
        if (attributes == null)
            return null;
        return attributes.get(attribute);
    }

    public void putAttribute(String attribute, Object value) {
        if (attributes == null)
            attributes = new LinkedHashMap<>();
        attributes.put(attribute, value);
    }

    public static interface FilterVisitor {
        public void visit(FilterExpression filter, Object value);
    }


    @Override
    @Deprecated
    public void setSortings(List<net.n2oapp.criteria.api.Sorting> sortings) {
        super.setSortings(sortings);
    }

    @Override
    @Deprecated
    public List<net.n2oapp.criteria.api.Sorting> getSortings() {
        return super.getSortings();
    }

    @Override
    @Deprecated
    public void setSorting(net.n2oapp.criteria.api.Sorting sorting) {
        super.setSorting(sorting);
    }

    @Override
    @Deprecated
    public net.n2oapp.criteria.api.Sorting getSorting() {
        return super.getSorting();
    }
}
