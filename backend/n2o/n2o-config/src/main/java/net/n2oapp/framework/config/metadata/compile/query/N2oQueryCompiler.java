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
import java.util.function.Function;
import java.util.stream.Collectors;

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
        query.setId(context.getSourceId((N2oCompileProcessor) p));
        if (source.getObjectId() != null) {
            query.setObject(p.getCompiled(new ObjectContext(source.getObjectId())));
        }
        query.setName(p.cast(source.getName(), source.getId()));
        query.setRoute(normalize(p.cast(source.getRoute(), source.getId())));
        query.setLists(initSeparators(source.getLists(), p));
        query.setUniques(initSeparators(source.getUniques(), p));
        query.setCounts(initSeparators(source.getCounts(), p));
        if (context.getValidations() != null && !context.getValidations().isEmpty())
            query.setValidations(context.getValidations());
        List<AbstractField> fields = source.getFields() != null ? Arrays.asList(source.getFields()) : List.of();
        initDefaultFilters(source.getFilters(), p);
        initDefaultFields(fields, null);
        initDefaultExpression(fields);
        replaceExpression(fields, source);

        compilePreFilters(source, p, context.getFilters());
        query.setDisplayFields(List.copyOf(fields));
        List<QuerySimpleField> simpleFields = source.getSimpleFields();
        query.setDisplayValues(Collections.unmodifiableMap(initDisplayValues(simpleFields)));
        query.setSortingFields(Collections.unmodifiableList(initSortingFields(simpleFields)));
        query.setFieldsMap(Collections.unmodifiableMap(initFieldsMap(fields, query.getId())));
        query.setSimpleFieldsMap(Collections.unmodifiableMap(initSimpleFieldsMap(simpleFields, query.getId())));
        query.setFieldNamesMap(Collections.unmodifiableMap(initFieldNamesMap(query.getSimpleFieldsMap())));
        query.setSortingSet(Collections.unmodifiableSet(initSortingSet(query.getSortingFields())));
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

    private Set<String> initSortingSet(List<QuerySimpleField> sortings) {
        return sortings.stream().map(QuerySimpleField::getId).collect(Collectors.toSet());
    }

    private void initExpressions(CompiledQuery query) {
        List<String> select = new ArrayList<>();
        List<String> join = new ArrayList<>();
        query.getDisplayFields().stream().filter(QuerySimpleField.class::isInstance).map(QuerySimpleField.class::cast).forEach(f -> {
            if (f.getSelectExpression() != null)
                select.add(f.getSelectExpression());
            if ((f.getNoJoin() == null || !f.getNoJoin()) && f.getJoinBody() != null) {
                join.add(f.getJoinBody());
            }
        });
        query.setSelectExpressions(select);
        query.setJoinExpressions(join);
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

    private void replaceExpression(List<AbstractField> fields, N2oQuery source) {
        for (AbstractField field : fields) {
            if (field instanceof QuerySimpleField) {
                QuerySimpleField simpleField = (QuerySimpleField) field;
                if ((simpleField.getExpression() == null)) continue;
                simpleField.setSelectExpression(replace(simpleField.getSelectExpression(), simpleField.getExpression()));
                simpleField.setSortingExpression(replace(simpleField.getSortingExpression(), simpleField.getExpression()));
                if (source.getFiltersList(field.getId()) != null)
                    for (N2oQuery.Filter filter : source.getFiltersList(field.getId())) {
                        filter.setText(replace(filter.getText(), simpleField.getExpression()));
                    }
            }
        }
    }

    private static String replace(String text, String expression) {
        if (text == null) return null;
        return text.replace(":expression", expression);
    }

    private void initDefaultFields(List<AbstractField> fields, String parentFieldId) {
        for (AbstractField field : fields) {
            String computedId = isNull(parentFieldId) ? field.getId() : parentFieldId + "." + field.getId();
            field.setAbsoluteId(computedId);
            if (field instanceof QueryReferenceField) {
                field.setMapping(castDefault(field.getMapping(), spel(field.getId())));
                initDefaultFields(Arrays.asList(((QueryReferenceField) field).getFields()), computedId);
            } else
                initDefaultSimpleField(((QuerySimpleField) field));
        }
    }

    private void initDefaultSimpleField(QuerySimpleField field) {
        field.setName(castDefault(field.getName(), field.getId()));
        field.setNoDisplay(castDefault(field.getNoDisplay(), false));
        field.setNoSorting(castDefault(field.getNoSorting(), false));
        field.setNoJoin(castDefault(field.getNoJoin(), false));

        if (!field.getNoDisplay()) {
            field.setMapping(castDefault(field.getMapping(), spel(field.getId())));
        }
        if (!field.getNoSorting()) {
            field.setSortingMapping(castDefault(field.getSortingMapping(), spel(field.getId() + "Direction")));
        }
    }

    private void initDefaultExpression(List<AbstractField> fields) {
        for (AbstractField field : fields) {
            if (field instanceof QuerySimpleField && ((QuerySimpleField) field).getExpression() == null) {
                ((QuerySimpleField) field).setExpression(field.getId());
            }
        }
    }

    public static Map<String, String> initDisplayValues(List<QuerySimpleField> displayFields) {
        Map<String, String> displayValues = new HashMap<>();
        for (QuerySimpleField field : displayFields) {
            if (field.getDefaultValue() != null) {
                displayValues.put(field.getId(), checkForNull(field.getDefaultValue()));
            }
        }
        return displayValues;
    }

    private static String checkForNull(String value) {
        if ("null".equals(value))
            value = null;
        return value;
    }

    private static List<QuerySimpleField> initSortingFields(List<QuerySimpleField> fields) {
        List<QuerySimpleField> result = new ArrayList<>();
        for (QuerySimpleField field : fields) {
            if (!field.getNoSorting()) {
                result.add(field);
            }
        }
        return result;
    }

    private static Map<String, QuerySimpleField> initSimpleFieldsMap(List<QuerySimpleField> fields, String id) {
        Map<String, QuerySimpleField> result = new StrictMap<>("Field '%s' in query '" + id + "' not found");
        for (QuerySimpleField field : fields)
            result.put(field.getAbsoluteId(), field);
        return result;
    }


    private static Map<String, String> initFieldNamesMap(Map<String, QuerySimpleField> fieldsMap) {
        return fieldsMap.keySet()
                .stream()
                .collect(Collectors.toMap(Function.identity(), k -> fieldsMap.get(k).getName()));
    }
}
