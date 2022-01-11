package net.n2oapp.framework.config.metadata.compile.query;


import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.data.validation.MandatoryValidation;
import net.n2oapp.framework.api.exception.SeverityType;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.dataprovider.N2oRestDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.local.util.CompileUtil;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;
import net.n2oapp.framework.api.metadata.meta.Filter;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.register.route.RouteUtil;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.spel;
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
        query.setId(source.getId());
        if (source.getObjectId() != null) {
            query.setObject(p.getCompiled(new ObjectContext(source.getObjectId())));
        }
        query.setName(p.cast(source.getName(), source.getId()));
        query.setRoute(normalize(p.cast(source.getRoute(), source.getId())));
        query.setLists(initSeparators(source.getLists(), p));
        query.setUniques(initSeparators(source.getUniques(), p));
        query.setCounts(initSeparators(source.getCounts(), p));
        query.setValidations(context.getValidations());
        List<N2oQuery.Field> fields = source.getFields() != null ? Arrays.asList(source.getFields()) : List.of();
        fields = initDefaultFields(fields);
        fields = initDefaultFilters(fields, p);
        fields = initDefaultMapping(fields);
        fields = initDefaultExpression(fields);
        fields = replaceExpression(fields);
        compilePreFilters(source, p, context.getFilters());
        query.setDisplayValues(Collections.unmodifiableMap(initDisplayValues(fields)));
        query.setDisplayFields(Collections.unmodifiableList(initDisplayFields(fields)));
        query.setSortingFields(Collections.unmodifiableList(initSortingFields(fields)));
        query.setFieldsMap(Collections.unmodifiableMap(initFieldsMap(fields, query.getId())));
        query.setFieldNamesMap(Collections.unmodifiableMap(initFieldNamesMap(query.getFieldsMap())));
        query.setSortingSet(Collections.unmodifiableSet(initSortingSet(query.getSortingFields())));
        query.setFiltersMap(Collections.unmodifiableMap(initFiltersMap(query, p)));
        query.setInvertFiltersMap(Collections.unmodifiableMap(initInvertFiltersMap(query.getFieldsMap())));
        query.setFilterFieldsMap(Collections.unmodifiableMap(initFilterFieldsMap(query.getFiltersMap())));
        query.setParamToFilterIdMap(Collections.unmodifiableMap(initParamToFilterIdMap(query.getFilterFieldsMap(), p)));
        query.setFilterIdToParamMap(Collections.unmodifiableMap(initFilterIdToParamMap(query.getParamToFilterIdMap())));
        query.setSubModelQueries(context.getSubModelQueries());
        initExpressions(query);
        query.setProperties(p.mapAttributes(source));
        query.setCopiedFields(context.getCopiedFields());
        return query;
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
        if (source.getFields() == null)
            return;
        for (Filter preFilter : preFilters) {
            for (N2oQuery.Field field : source.getFields()) {
                if (field.getFilterList() != null) {
                    for (N2oQuery.Filter filter : field.getFilterList()) {
                        if (filter.getFilterField().equals(preFilter.getFilterId())) {
                            filter.setParam(p.cast(preFilter.getParam(), filter.getParam()));
                            if (preFilter.getLink() != null && !preFilter.getLink().isLink()) {
                                filter.setCompiledDefaultValue(p.cast(preFilter.getLink().getValue(), p.resolve(filter.getDefaultValue(), filter.getDomain())));
                            }
                        }
                    }
                }
            }
        }
    }

    private Map<String, Map.Entry<String, FilterType>> initInvertFiltersMap(Map<String, N2oQuery.Field> fieldsMap) {
        Map<String, Map.Entry<String, FilterType>> invertFiltersMap = new StrictMap<>();
        fieldsMap.values().stream().filter(queryField -> !queryField.isSearchUnavailable()).forEach(queryField -> {
            for (N2oQuery.Filter f : queryField.getFilterList()) {
                invertFiltersMap.put(f.getFilterField(), new CompiledQuery.FilterEntry(queryField.getId(), f.getType()));
            }
        });
        return invertFiltersMap;
    }

    private Map<String, String> initParamToFilterIdMap(Map<String, N2oQuery.Filter> filterIdsMap, CompileProcessor p) {
        Map<String, String> filterParams = new StrictMap<>();
        for (N2oQuery.Filter filter : filterIdsMap.values()) {
            String param = p.cast(filter.getParam(), RouteUtil.normalizeParam(filter.getFilterField()));
            filterParams.put(param, filter.getFilterField());
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
                result.put(filter.getFilterField(), filter);
            }
        }
        return result;
    }

    private Set<String> initSortingSet(List<N2oQuery.Field> sortings) {
        return sortings.stream().map(N2oQuery.Field::getId).collect(Collectors.toSet());
    }

    private void initExpressions(CompiledQuery query) {
        List<String> select = new ArrayList<>();
        List<String> join = new ArrayList<>();
        query.getDisplayFields().forEach(f -> {
            if (f.getSelectBody() != null)
                select.add(f.getSelectBody());
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


    private Map<String, Map<FilterType, N2oQuery.Filter>> initFiltersMap(CompiledQuery query, CompileProcessor p) {
        Map<String, Map<FilterType, N2oQuery.Filter>> result = new HashMap<>();
        query.getFieldsMap().values().stream().filter(queryField -> !queryField.isSearchUnavailable()).forEach(queryField -> {
            Map<FilterType, N2oQuery.Filter> filters = new HashMap<>();
            for (N2oQuery.Filter f : queryField.getFilterList()) {
                if (f.getDomain() == null) {
                    f.setDomain(DomainProcessor.getDomain(queryField.getDomain(), f.getType()));
                }
                if (f.getCompiledDefaultValue() == null && f.getDefaultValue() != null) {
                    f.setCompiledDefaultValue(p.resolve(f.getDefaultValue(), f.getDomain()));
                }
                filters.put(f.getType(), f);
                if (f.getRequired() != null && f.getRequired()) {
                    MandatoryValidation mandatory = new MandatoryValidation(
                            f.getFilterField(),
                            p.getMessage("n2o.required.filter"),
                            f.getFilterField()
                    );
                    mandatory.setMoment(N2oValidation.ServerMoment.beforeQuery);
                    mandatory.setSeverity(SeverityType.danger);
                    if (query.getValidations() == null)
                        query.setValidations(new ArrayList<>());
                    query.getValidations().add(mandatory);
                }
            }
            result.put(queryField.getId(), filters);
        });
        return result;
    }

    private List<N2oQuery.Field> initDefaultFilters(List<N2oQuery.Field> fields, CompileProcessor p) {
        for (N2oQuery.Field field : fields) {
            if (field.getFilterList() != null)
                for (N2oQuery.Filter filter : field.getFilterList()) {
                    filter.setFilterField(p.cast(filter.getFilterField(), RouteUtil.normalizeParam(field.getId()) + "_" + filter.getType()));
                    filter.setParam(p.cast(filter.getParam(), RouteUtil.normalizeParam(filter.getFilterField())));
                }
        }
        return fields;
    }

    private List<N2oQuery.Field> replaceExpression(List<N2oQuery.Field> fields) {
        for (N2oQuery.Field field : fields) {
            if ((field.getExpression() == null)) continue;
            field.setSelectBody(replace(field.getSelectBody(), field.getExpression()));
            field.setSortingBody(replace(field.getSortingBody(), field.getExpression()));
            if (field.getFilterList() != null)
                for (N2oQuery.Filter filter : field.getFilterList()) {
                    filter.setText(replace(filter.getText(), field.getExpression()));
                }
        }
        return fields;
    }

    private static String replace(String text, String expression) {
        if (text == null) return null;
        return text.replace(":expression", expression);
    }

    private List<N2oQuery.Field> initDefaultFields(List<N2oQuery.Field> fields) {
        for (N2oQuery.Field field : fields) {
            field.setName(CompileUtil.castDefault(field.getName(), field.getId()));
            field.setNoDisplay(CompileUtil.castDefault(field.getNoDisplay(), false));
            field.setNoSorting(CompileUtil.castDefault(field.getNoSorting(), false));
            field.setNoJoin(CompileUtil.castDefault(field.getNoJoin(), false));
            if (field.getFilterList() == null)
                field.setFilterList(new N2oQuery.Filter[]{});
        }
        return fields;
    }

    private List<N2oQuery.Field> initDefaultMapping(List<N2oQuery.Field> fields) {
        for (N2oQuery.Field field : fields) {
            if (!field.getNoDisplay() && field.getSelectMapping() == null)
                field.setSelectMapping(spel(field.getId()));
            if (!field.getNoSorting() && field.getSortingMapping() == null)
                field.setSortingMapping(spel(field.getId() + "Direction"));
            for (N2oQuery.Filter filter : field.getFilterList()) {
                if (filter.getMapping() == null)
                    filter.setMapping(spel(filter.getFilterField()));
            }
        }
        return fields;
    }

    private List<N2oQuery.Field> initDefaultExpression(List<N2oQuery.Field> fields) {
        for (N2oQuery.Field field : fields) {
            if (field.getExpression() == null) {
                field.setExpression(field.getId());
            }
        }
        return fields;
    }

    public static Map<String, String> initDisplayValues(List<N2oQuery.Field> displayFields) {
        Map<String, String> displayValues = new HashMap<>();
        for (N2oQuery.Field field : displayFields) {
            if (field.getSelectDefaultValue() != null) {
                displayValues.put(field.getId(), checkForNull(field.getSelectDefaultValue()));
            }
        }
        return displayValues;
    }

    private static String checkForNull(String value) {
        if ("null".equals(value))
            value = null;
        return value;
    }

    private static List<N2oQuery.Field> initSortingFields(List<N2oQuery.Field> fields) {
        List<N2oQuery.Field> result = new ArrayList<>();
        for (N2oQuery.Field field : fields) {
            if (!field.getNoSorting()) {
                result.add(field);
            }
        }
        return result;
    }

    private static List<N2oQuery.Field> initDisplayFields(List<N2oQuery.Field> fields) {
        List<N2oQuery.Field> result = new ArrayList<>();
        for (N2oQuery.Field field : fields) {
            if (!field.getNoDisplay()) {
                result.add(field);
            }
        }
        return result;
    }

    private static  Map<String, N2oQuery.Field> initFieldsMap(List<N2oQuery.Field> fields, String id) {
        Map<String, N2oQuery.Field> result = new StrictMap<>("Field '%s' in query '" + id + "' not found");
        for (N2oQuery.Field field : fields) {
            result.put(field.getId(), field);
        }
        return result;
    }


    private static Map<String, String> initFieldNamesMap(Map<String, N2oQuery.Field> fieldsMap) {
        return fieldsMap.values()
                .stream()
                .collect(Collectors.toMap(N2oQuery.Field::getId, N2oQuery.Field::getName));
    }


}
