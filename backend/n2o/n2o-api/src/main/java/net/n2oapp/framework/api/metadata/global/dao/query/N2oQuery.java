package net.n2oapp.framework.api.metadata.global.dao.query;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.aware.NameAware;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oInvocation;
import net.n2oapp.framework.api.metadata.global.dao.query.field.QueryReferenceField;
import net.n2oapp.framework.api.metadata.global.dao.query.field.QuerySimpleField;
import org.springframework.lang.NonNull;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;

/**
 * Source модель запроса за данными
 */
@Getter
@Setter
public class N2oQuery extends N2oMetadata implements NameAware, ExtensionAttributesAware {

    protected AbstractField[] fields;
    private String name;
    private String objectId;
    private String route;
    private Selection[] lists;
    private Selection[] uniques;
    private Selection[] counts;
    private Filter[] filters;
    private Map<String, Filter[]> filtersMap;
    private Map<N2oNamespace, Map<String, String>> extAttributes;

    @Override
    public final String getPostfix() {
        return "query";
    }

    @Override
    public final Class<? extends N2oMetadata> getSourceBaseClass() {
        return N2oQuery.class;
    }

    public QuerySimpleField getSimpleFieldByAbsoluteId(@NonNull String absoluteId) {
        if (isEmpty(fields))
            return null;
        if (isNull(fields[0].getAbsoluteId()))
            initAbsoluteIds();

        return getSimpleFields().stream().filter(f -> absoluteId.equals(f.getAbsoluteId())).findFirst().orElse(null);
    }

    public void initAbsoluteIds() {
        if (isNotEmpty(fields))
            initAbsoluteId(fields, null);
    }

    public void initAbsoluteId(AbstractField[] fields, String parentId) {
        for (AbstractField field : fields) {
            String computedId = isNull(parentId) ? field.getId() : parentId + "." + field.getId();
            field.setAbsoluteId(computedId);

            if (field instanceof QueryReferenceField && isNotEmpty(((QueryReferenceField) field).getFields()))
                initAbsoluteId(((QueryReferenceField) field).getFields(), computedId);
        }
    }

    public List<QuerySimpleField> getSimpleFields() {
        List<QuerySimpleField> result = new ArrayList<>();
        collectSimpleFields(fields, result);

        return result;
    }

    private void collectSimpleFields(AbstractField[] fields, List<QuerySimpleField> simpleFields) {
        if (isNull(fields))
            return;
        for (AbstractField field : fields) {
            if (field instanceof QueryReferenceField)
                collectSimpleFields(((QueryReferenceField) field).getFields(), simpleFields);
            else
                simpleFields.add((QuerySimpleField) field);
        }
    }

    /**
     * Проверка наличия хотя бы одного фильтра с соответствующим идентификатором поля
     *
     * @param fieldId идентификатор поля
     * @return true - существует хотя бы один фильтр, false - соответствующих фильтров нет, либо в выборке нет фильтров
     */
    public boolean isSearchAvailable(String fieldId) {
        if (isNull(filters))
            return false;

        return Arrays.stream(filters).anyMatch(filter -> fieldId.equals(filter.getFieldId()));
    }

    public Filter[] getFiltersList(String fieldId) {
        if (isNull(filters))
            return new Filter[0];
        if (isNull(filtersMap)) {
            Map<String, List<Filter>> filtersListMap = new HashMap<>();
            for (Filter filter : filters) {
                filtersListMap.putIfAbsent(filter.getFieldId(), new ArrayList<>());
                filtersListMap.get(filter.getFieldId()).add(filter);
            }
            setFiltersMap(
                    filtersListMap.keySet()
                            .stream()
                            .collect(
                                    Collectors.toMap(
                                            Function.identity(),
                                            k -> filtersListMap.get(k).toArray(new Filter[0])
                                    )
                            )
            );
        }

        return filtersMap.getOrDefault(fieldId, new Filter[0]);
    }

    @Getter
    @Setter
    public static class Filter implements Source, Compiled {

        private String text;
        private String defaultValue;
        private Object compiledDefaultValue;
        private FilterType type;
        private String filterId;
        private String fieldId;
        private String domain;
        private String normalize;
        private String mapping;
        private Boolean required;

        /**
         * Параметр в адресе маршрутов выборки
         */
        private String param;
        private boolean generated = false;

        public Filter(String filterId, FilterType type) {
            this.filterId = filterId;
            this.type = type;
        }

        public Filter(String filterId, FilterType type, String filterBody) {
            this.text = filterBody;
            this.filterId = filterId;
            this.type = type;
        }

        public Filter(String filterId, FilterType type, String filterBody, String domain) {
            this.filterId = filterId;
            this.type = type;
            this.text = filterBody;
            this.domain = domain;
        }

        public Filter(String filterId, FilterType type, String filterBody, String domain, String normalize) {
            this.type = type;
            this.filterId = filterId;
            this.domain = domain;
            this.normalize = normalize;
            this.text = filterBody;
        }

        public Filter() {
        }
    }

    @Getter
    @Setter
    public static class Selection implements Source, Compiled {

        private String[] filters;
        private String resultMapping;
        private String resultNormalize;
        private String countMapping;
        private String ascExpression;
        private String descExpression;
        private String additionalMapping;
        private N2oInvocation invocation;
        private Type type;

        public Selection(Type type) {
            this.type = type;
        }

        public Selection(Type type, N2oInvocation invocation) {
            this(type);
            this.invocation = invocation;
        }


        public enum Type {
            list, unique, count
        }
    }

    public void adapterV4() {
        if (nonNull(fields)) {
            List<Filter> filters = new ArrayList<>();
            for (AbstractField field : fields) {
                if (nonNull(((QuerySimpleField) field).getFilterList())) {
                    for (Filter filter : ((QuerySimpleField) field).getFilterList()) {
                        filter.setFieldId(field.getId());
                        filters.add(filter);
                    }
                }
            }
            setFilters(filters.toArray(new Filter[0]));
        }
    }
}
