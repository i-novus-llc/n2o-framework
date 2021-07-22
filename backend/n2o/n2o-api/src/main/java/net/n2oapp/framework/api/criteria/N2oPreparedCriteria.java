package net.n2oapp.framework.api.criteria;

import net.n2oapp.criteria.api.Criteria;
import net.n2oapp.criteria.dataset.DataSet;

import java.util.*;

/**
 * Критерий фильтрации данных
 */
public class N2oPreparedCriteria extends Criteria {

    public static N2oPreparedCriteria simpleCriteriaOneRecord(String fieldId, Object fieldValue) {
        return new N2oPreparedCriteria(fieldId, fieldValue, 1);
    }

    private List<Restriction> restrictions;
    private Map<String, Object> additionalFields;
    private Map<String, Object> attributes;

    public N2oPreparedCriteria() {
    }

    public N2oPreparedCriteria(N2oPreparedCriteria base) {
        super(base);
        this.restrictions = new ArrayList<>(base.getRestrictions());
        this.additionalFields = new LinkedHashMap<>(base.getAdditionalFields());
        this.attributes = new LinkedHashMap<>(base.getAttributes());
    }

    @SuppressWarnings("unchecked")
    public N2oPreparedCriteria(String fieldId, Object fieldValue, int size) {
        super();
        setSize(size);
        setSortings(Collections.EMPTY_LIST);
        addRestriction(new Restriction(fieldId, fieldValue));
    }

    public Map<String, Object> getAdditionalFields() {
        if (additionalFields == null)
            return Collections.emptyMap();
        return additionalFields;
    }


    public void addRestriction(Restriction restriction) {
        if (restrictions == null)
            restrictions = new ArrayList<>();
        this.restrictions.add(restriction);
    }

    public List<Restriction> getRestrictions() {
        if (restrictions == null)
            return Collections.emptyList();
        return restrictions;
    }

    public Map<String, Object> getAttributes() {
        if (attributes == null)
            return Collections.emptyMap();
        return Collections.unmodifiableMap(attributes);
    }

    public void putAdditionalField(String field, Object value) {
        if (additionalFields == null)
            additionalFields = new LinkedHashMap<>();
        this.additionalFields.put(field, value);
    }

    public void removeFilterForField(String fieldId) {
        if (restrictions != null) {
            for (Restriction restriction : new ArrayList<>(restrictions)) {
                if (restriction.getFieldId().equals(fieldId))
                    restrictions.remove(restriction);
            }
        }
    }

    public void addRestrictions(List<Restriction> restrictions) {
        for (Restriction restriction : restrictions) {
            addRestriction(restriction);
        }
    }

    public boolean containsRestriction(String fieldId) {
        for (Restriction restriction : getRestrictions()) {
            if (restriction.getFieldId().equals(fieldId))
                return true;
        }
        return false;
    }

    public List<Restriction> getRestrictions(String fieldId) {
        List<Restriction> res = new ArrayList<>();
        for (Restriction restriction : getRestrictions()) {
            if (restriction.getFieldId().equals(fieldId))
                res.add(restriction);
        }
        return res;
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

    //toDo спорный метод, нужен для процессинга
    public DataSet toDataSet() {
        DataSet dataSet = new DataSet();
        for (Restriction restriction : getRestrictions()) {
            dataSet.put(restriction.getFieldId(), restriction.getValue());
        }
        for (String field : getAdditionalFields().keySet()) {
            try {
                dataSet.put(field, getAdditionalFields().get(field));
            } catch (Exception e) {
                // этот метод вообще странный
            }
        }
        return dataSet;
    }
}
