package net.n2oapp.framework.api.metadata.local;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.aware.PropertiesAware;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.dao.query.AbstractField;
import net.n2oapp.framework.api.metadata.global.dao.query.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.query.field.QueryReferenceField;
import net.n2oapp.framework.api.metadata.global.dao.query.field.QuerySimpleField;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;
import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Скомпилированная модель запроса за данными
 */
@Getter
@Setter
public class CompiledQuery implements Compiled, IdAware, PropertiesAware {
    private N2oQuery.Selection[] lists;
    private N2oQuery.Selection[] uniques;
    private N2oQuery.Selection[] counts;

    private String name;
    private String route;
    private CompiledObject object;
    protected List<AbstractField> displayFields;
    private List<QuerySimpleField> sortingFields;
    private Map<String, Object> properties;
    private List<Validation> validations;
    private List<SubModelQuery> subModelQueries;

    protected Map<String, AbstractField> fieldsMap;//absoluteId to field
    protected Map<String, QuerySimpleField> simpleFieldsMap;
    private List<String> selectExpressions; // fieldId - select body
    protected String id;

    private Map<String, Map<FilterType, N2oQuery.Filter>> filtersMap = new StrictMap<>(); //[fieldId : [filterType : filterId]]
    private Map<String, Map.Entry<String, FilterType>> invertFiltersMap = new StrictMap<>(); //[filterId : [fieldId, filterType]]
    private Map<String, N2oQuery.Filter> filterFieldsMap = new StrictMap<>(); //[filterId : filter]
    private Map<String, String> paramToFilterIdMap = new StrictMap<>(); // [urlParam : filterId]
    private Map<String, String> filterIdToParamMap = new StrictMap<>(); // [filterId : urlParam]
    private Set<String> copiedFields;

    public List<AbstractField> getDisplayedInnerFields(QueryReferenceField referenceField) {
        if (referenceField.getFields() == null)
            return new ArrayList<>();
        return Arrays.stream(referenceField.getFields()).filter(AbstractField::getIsSelected).collect(Collectors.toList());
    }

    public boolean containsFilter(String fieldId, FilterType type) {
        return filtersMap.get(fieldId) != null && filtersMap.get(fieldId).containsKey(type);
    }

    public N2oQuery.Filter getFilterByPreFilter(N2oPreFilter preFilter) {
        return filtersMap.containsKey(preFilter.getFieldId()) ?
                filtersMap.get(preFilter.getFieldId()).get(preFilter.getType())
                : null;
    }

    public String getFilterFieldId(String fieldId, FilterType type) {
        return filtersMap.get(fieldId) == null || filtersMap.get(fieldId).get(type) == null
                ? null : filtersMap.get(fieldId).get(type).getFilterId();
    }

    public static class FilterEntry implements Map.Entry<String, FilterType>, Serializable {
        private String fieldId;
        private FilterType value;

        public FilterEntry(String fieldId, FilterType value) {
            this.fieldId = fieldId;
            this.value = value;
        }

        @Override
        public String getKey() {
            return fieldId;
        }

        @Override
        public FilterType getValue() {
            return value;
        }

        @Override
        public FilterType setValue(FilterType value) {
            this.value = value;
            return value;
        }
    }
}
