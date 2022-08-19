package net.n2oapp.framework.config.metadata.compile.query;


import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.data.validation.MandatoryValidation;
import net.n2oapp.framework.api.exception.SeverityType;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.dataprovider.N2oRestDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.query.AbstractField;
import net.n2oapp.framework.api.metadata.global.dao.query.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.query.field.QueryReferenceField;
import net.n2oapp.framework.api.metadata.global.dao.query.field.QuerySimpleField;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;
import net.n2oapp.framework.api.metadata.meta.Filter;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.metadata.compile.N2oCompileProcessor;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.register.route.RouteUtil;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static net.n2oapp.framework.api.DynamicUtil.isDynamic;
import static java.util.Objects.isNull;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.spel;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.register.route.RouteUtil.normalize;

/**
 * Компиляция query
 */
@Component
public class N2oQueryCompiler implements BaseSourceCompiler<CompiledQuery, N2oQuery, QueryContext> {

    @Override
    public Class<N2oQuery> getSourceClass() {
        return N2oQuery.class;
    }

    @Override
    public CompiledQuery compile(N2oQuery source, QueryContext context, CompileProcessor p) {
        CompiledQuery query = new CompiledQuery();
        String queryId = context.getSourceId((N2oCompileProcessor) p);
        query.setId(queryId);
        if (source.getObjectId() != null) {
            query.setObject(p.getCompiled(new ObjectContext(source.getObjectId())));
        }
        query.setName(p.cast(source.getName(), source.getId()));

        String route = normalize(p.cast(source.getRoute(), queryId));
        query.setRoute(isDynamic(route) ? route.replaceAll("[?=&]", "_") : route);

        query.setLists(initSeparators(source.getLists(), p));
        query.setUniques(initSeparators(source.getUniques(), p));
        query.setCounts(initSeparators(source.getCounts(), p));
        if (context.getValidations() != null && !context.getValidations().isEmpty())
            query.setValidations(context.getValidations());
        List<AbstractField> fields = source.getFields() != null ? Arrays.asList(source.getFields()) : List.of();
        initDefaultFilters(source.getFilters(), p);
        initDefaultFields(fields, null,
                p.resolve(property(("n2o.api.query.field.is_selected")), Boolean.class),
                p.resolve(property(("n2o.api.query.field.is_sorted")), Boolean.class));

        compilePreFilters(source, p, context.getFilters());
        query.setDisplayFields(Collections.unmodifiableList(initDisplayFields(fields)));
        List<QuerySimpleField> simpleFields = source.getSimpleFields();
        query.setSortingFields(Collections.unmodifiableList(initSortingFields(simpleFields)));
        query.setFieldsMap(Collections.unmodifiableMap(initFieldsMap(fields, query.getId())));
        query.setSimpleFieldsMap(Collections.unmodifiableMap(initSimpleFieldsMap(simpleFields, query.getId())));
        query.setFiltersMap(Collections.unmodifiableMap(initFiltersMap(source, query, p)));
        query.setInvertFiltersMap(Collections.unmodifiableMap(initInvertFiltersMap(source, query.getSimpleFieldsMap())));
        query.setFilterFieldsMap(Collections.unmodifiableMap(initFilterFieldsMap(query.getFiltersMap())));
        query.setParamToFilterIdMap(Collections.unmodifiableMap(initParamToFilterIdMap(query.getFilterFieldsMap(), p)));
        query.setFilterIdToParamMap(Collections.unmodifiableMap(initFilterIdToParamMap(query.getParamToFilterIdMap())));
        query.setSubModelQueries(context.getSubModelQueries());
        initExpressions(query);
        query.setProperties(p.mapAttributes(source));
        query.setCopiedFields(context.getCopiedFields());
        return query;
    }

    private static List<AbstractField> initDisplayFields(List<AbstractField> fields) {
        return fields.stream().filter(AbstractField::getIsSelected).collect(Collectors.toList());
    }

    private static Map<String, AbstractField> initFieldsMap(List<AbstractField> fields, String id) {
        Map<String, AbstractField> result = new StrictMap<>("Field '%s' in query '" + id + "' not found");
        for (AbstractField field : fields) {
            result.put(field.getAbsoluteId(), field);
            if (field instanceof QueryReferenceField)
                result.putAll(initFieldsMap(Arrays.asList(((QueryReferenceField) field).getFields()), id));
        }
        return result;
    }

    /**
     * Предустановка значений по умолчанию и URL параметров в фильтры исходной выборки
     *
     * @param source     Исходная выборка
     * @param p          Процессор сборки
     * @param preFilters Предустановленны фильтры
     */
    private void compilePreFilters(N2oQuery source, CompileProcessor p, List<Filter> preFilters) {
        if (preFilters == null)
            return;
        if (source.getFilters() == null)
            return;
        for (Filter preFilter : preFilters) {
            for (N2oQuery.Filter filter : source.getFilters()) {
                if (filter.getFilterId().equals(preFilter.getFilterId())) {
                    filter.setParam(p.cast(preFilter.getParam(), filter.getParam()));
                    if (preFilter.getLink() != null && !preFilter.getLink().isLink()) {
                        filter.setCompiledDefaultValue(p.cast(preFilter.getLink().getValue(), p.resolve(filter.getDefaultValue(), filter.getDomain())));
                    }
                }
            }
        }
    }

    private Map<String, Map.Entry<String, FilterType>> initInvertFiltersMap(N2oQuery source, Map<String, QuerySimpleField> fieldsMap) {
        Map<String, Map.Entry<String, FilterType>> invertFiltersMap = new StrictMap<>();
        fieldsMap.values().stream().filter(queryField -> source.isSearchAvailable(queryField.getId())).forEach(queryField -> {
            for (N2oQuery.Filter f : source.getFiltersList(queryField.getId())) {
                invertFiltersMap.put(f.getFilterId(), new CompiledQuery.FilterEntry(queryField.getId(), f.getType()));
            }
        });
        return invertFiltersMap;
    }

    private Map<String, String> initParamToFilterIdMap(Map<String, N2oQuery.Filter> filterIdsMap, CompileProcessor p) {
        Map<String, String> filterParams = new StrictMap<>();
        for (N2oQuery.Filter filter : filterIdsMap.values()) {
            String param = p.cast(filter.getParam(), RouteUtil.normalizeParam(filter.getFilterId()));
            filterParams.put(param, filter.getFilterId());
        }
        return filterParams;
    }

    private Map<String, String> initFilterIdToParamMap(Map<String, String> paramToFilterMap) {
        Map<String, String> filterParams = new StrictMap<>();
        for (Map.Entry<String, String> paramEntry : paramToFilterMap.entrySet()) {
            filterParams.put(paramEntry.getValue(), paramEntry.getKey());
        }
        return filterParams;
    }

    private Map<String, N2oQuery.Filter> initFilterFieldsMap(Map<String, Map<FilterType, N2oQuery.Filter>> filtersMap) {
        Map<String, N2oQuery.Filter> result = new StrictMap<>();
        for (Map<FilterType, N2oQuery.Filter> filterMap : filtersMap.values()) {
            for (N2oQuery.Filter filter : filterMap.values()) {
                result.put(filter.getFilterId(), filter);
            }
        }
        return result;
    }

    private void initExpressions(CompiledQuery query) {
        query.setSelectExpressions(query.getDisplayFields().stream()
                .map(AbstractField::getSelectExpression)
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
    }

    private N2oQuery.Selection[] initSeparators(N2oQuery.Selection[] selections, CompileProcessor p) {
        if (selections != null) {
            for (N2oQuery.Selection selection : selections) {
                if (selection.getInvocation() instanceof N2oRestDataProvider) {
                    N2oRestDataProvider invocation = (N2oRestDataProvider) selection.getInvocation();
                    invocation.setFiltersSeparator(p.cast(invocation.getFiltersSeparator(),
                            p.resolve(property("n2o.config.rest.filters_separator"), String.class)));
                    invocation.setSelectSeparator(p.cast(invocation.getSelectSeparator(),
                            p.resolve(property("n2o.config.rest.select_separator"), String.class)));
                    invocation.setJoinSeparator(p.cast(invocation.getJoinSeparator(), p.resolve(property(
                            "n2o.config.rest.join_separator"), String.class)));
                    invocation.setSortingSeparator(p.cast(invocation.getSortingSeparator(),
                            p.resolve(property("n2o.config.rest.sorting_separator"), String.class)));

                }
            }
        }
        return selections;
    }


    private Map<String, Map<FilterType, N2oQuery.Filter>> initFiltersMap(N2oQuery source, CompiledQuery query, CompileProcessor p) {
        Map<String, Map<FilterType, N2oQuery.Filter>> result = new HashMap<>();
        query.getFieldsMap().keySet().stream().filter(source::isSearchAvailable).forEach(key -> {
            AbstractField queryField = query.getFieldsMap().get(key);
            Map<FilterType, N2oQuery.Filter> filters = new HashMap<>();
            for (N2oQuery.Filter f : source.getFiltersList(queryField.getAbsoluteId())) {
                if (f.getDomain() == null && queryField instanceof QuerySimpleField) {
                    f.setDomain(DomainProcessor.getDomain(((QuerySimpleField) queryField).getDomain(), f.getType()));
                }
                if (f.getCompiledDefaultValue() == null && f.getDefaultValue() != null) {
                    f.setCompiledDefaultValue(p.resolve(f.getDefaultValue(), f.getDomain()));
                }
                filters.put(f.getType(), f);
                if (f.getRequired() != null && f.getRequired()) {
                    MandatoryValidation mandatory = new MandatoryValidation(
                            f.getFilterId(),
                            p.getMessage("n2o.required.filter"),
                            f.getFilterId()
                    );
                    mandatory.setMoment(N2oValidation.ServerMoment.beforeQuery);
                    mandatory.setSeverity(SeverityType.danger);
                    if (query.getValidations() == null)
                        query.setValidations(new ArrayList<>());
                    query.getValidations().add(mandatory);
                }
            }
            result.put(queryField.getAbsoluteId(), filters);
        });
        return result;
    }

    private void initDefaultFilters(N2oQuery.Filter[] filters, CompileProcessor p) {
        if (filters != null) {
            for (N2oQuery.Filter filter : filters) {
                filter.setFilterId(p.cast(filter.getFilterId(), RouteUtil.normalizeParam(filter.getFieldId()) + "_" + filter.getType()));
                filter.setParam(p.cast(filter.getParam(), RouteUtil.normalizeParam(filter.getFilterId())));
                if (filter.getMapping() == null)
                    filter.setMapping(spel(filter.getFilterId()));
            }
        }
    }

    private void initDefaultFields(List<AbstractField> fields, String parentFieldId, Boolean defaultSelected,
                                   Boolean defaultSorted) {
        for (AbstractField field : fields) {
            String computedId = isNull(parentFieldId) ? field.getId() : parentFieldId + "." + field.getId();
            field.setAbsoluteId(computedId);
            field.setIsSelected(castDefault(field.getIsSelected(), defaultSelected));

            if (field.getIsSelected()) {
                field.setMapping(castDefault(field.getMapping(), spel(field.getId())));
            }
            if (field instanceof QueryReferenceField) {
                field.setMapping(castDefault(field.getMapping(), spel(field.getId())));
                initDefaultFields(Arrays.asList(((QueryReferenceField) field).getFields()), computedId, defaultSelected, defaultSorted);
            }
            else
                initDefaultSimpleField(((QuerySimpleField) field), defaultSorted);
        }
    }

    private void initDefaultSimpleField(QuerySimpleField field, Boolean defaultSorted) {
        field.setName(castDefault(field.getName(), field.getId()));
        field.setIsSorted(castDefault(field.getIsSorted(), defaultSorted));

        if (field.getIsSorted()) {
            field.setSortingMapping(castDefault(field.getSortingMapping(), spel(field.getId() + "Direction")));
        }
    }

    private static List<QuerySimpleField> initSortingFields(List<QuerySimpleField> fields) {
        List<QuerySimpleField> result = new ArrayList<>();
        for (QuerySimpleField field : fields) {
            if (field.getIsSorted()) {
                result.add(field);
            }
        }
        return result;
    }

    private static Map<String, QuerySimpleField> initSimpleFieldsMap(List<QuerySimpleField> fields, String id) {
        Map<String, QuerySimpleField> result = new StrictMap<>("Field '%s' in query '" + id + "' not found");
        for (QuerySimpleField field : fields) {
            result.put(field.getAbsoluteId(), field);
        }
        return result;
    }
}
