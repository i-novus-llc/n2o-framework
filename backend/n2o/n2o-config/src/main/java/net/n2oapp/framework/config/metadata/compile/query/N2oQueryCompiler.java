package net.n2oapp.framework.config.metadata.compile.query;


import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectScalarField;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.local.util.CompileQueryUtil;
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

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.colon;
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
        query.setName(CompileUtil.castDefault(source.getName(), source.getId()));
        query.setRoute(normalize(p.cast(source.getRoute(), source.getId())));
        query.setLists(source.getLists());
        query.setCounts(source.getCounts());
        query.setUniques(source.getUniques());
        query.setValidations(context.getValidations());
        List<N2oQuery.Field> fields = Arrays.asList(source.getFields());
        fields = initDefaultByObject(fields, query.getObject());
        fields = initDefaultFields(fields);
        fields = initDefaultFilters(fields, p);
        fields = initDefaultMapping(fields);
        fields = initDefaultBody(fields);
        fields = replaceExpression(fields);
        compilePreFilters(source, p, context.getFilters());
        query.setDisplayValues(Collections.unmodifiableMap(CompileQueryUtil.initDisplayValues(fields)));
        query.setDisplayFields(Collections.unmodifiableList(CompileQueryUtil.initDisplayFields(fields)));
        query.setSortingFields(Collections.unmodifiableList(CompileQueryUtil.initSortingFields(fields)));
        query.setFieldsMap(Collections.unmodifiableMap(CompileQueryUtil.initFieldsMap(fields, query.getId())));
        query.setFieldNamesMap(Collections.unmodifiableMap(CompileQueryUtil.initFieldNamesMap(query.getFieldsMap())));
        query.setSortingSet(Collections.unmodifiableSet(initSortingSet(query.getSortingFields())));
        query.setFiltersMap(Collections.unmodifiableMap(initFiltersMap(query.getFieldsMap(), p)));
        query.setInvertFiltersMap(Collections.unmodifiableMap(initInvertFiltersMap(query.getFieldsMap())));
        query.setFilterFieldsMap(Collections.unmodifiableMap(initFilterFieldsMap(query.getFiltersMap())));
        query.setParamToFilterIdMap(Collections.unmodifiableMap(initParamToFilterIdMap(query.getFilterFieldsMap(), p)));
        query.setFilterIdToParamMap(Collections.unmodifiableMap(initFilterIdToParamMap(query.getParamToFilterIdMap())));
        query.setSubModelQueries(context.getSubModelQueries());
        initExpressions(query);
        query.setProperties(p.mapAttributes(source));
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
                            if (preFilter.getLink() != null && !preFilter.getLink().isConst()) {
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
            select.add(f.getSelectBody());
            if ((f.getNoJoin() == null || !f.getNoJoin()) && f.getJoinBody() != null) {
                join.add(f.getJoinBody());
            }
        });
        query.setSelectExpressions(select);
        query.setJoinExpressions(join);
    }


    private Map<String, Map<FilterType, N2oQuery.Filter>> initFiltersMap(Map<String, N2oQuery.Field> fieldsMap, CompileProcessor p) {
        Map<String, Map<FilterType, N2oQuery.Filter>> result = new HashMap<>();
        fieldsMap.values().stream().filter(queryField -> !queryField.isSearchUnavailable()).forEach(queryField -> {
            Map<FilterType, N2oQuery.Filter> filters = new HashMap<>();
            for (N2oQuery.Filter f : queryField.getFilterList()) {
                if (f.getDomain() == null) {
                    f.setDomain(DomainProcessor.getDomain(queryField.getDomain(), f.getType()));
                }
                if (f.getCompiledDefaultValue() == null && f.getDefaultValue() != null) {
                    f.setCompiledDefaultValue(p.resolve(f.getDefaultValue(), f.getDomain()));
                }
                filters.put(f.getType(), f);
            }
            result.put(queryField.getId(), filters);
        });
        return result;
    }


    private List<N2oQuery.Field> initDefaultByObject(List<N2oQuery.Field> fields, CompiledObject object) {
        if (object == null) return fields;
        Map<String, AbstractParameter> objectFields = object.getObjectFieldsMap();
        for (N2oQuery.Field field : fields) {
            if (!objectFields.containsKey(field.getId()))
                continue;
            AbstractParameter of = objectFields.get(field.getId());
            if (field.getDomain() == null) {
                String domain = "string";
                if ((of instanceof ObjectScalarField) && (((ObjectScalarField) of).getDomain() != null)) {
                    domain = ((ObjectScalarField) of).getDomain();
                }
                field.setDomain(domain);
            }
            if (field.getName() == null) {
                field.setName(of.getName());
            }
            if (field.getSelectMapping() == null)
                field.setSelectMapping(of.getMapping());
        }
        return fields;
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

    private List<N2oQuery.Field> initDefaultBody(List<N2oQuery.Field> fields) {
        for (N2oQuery.Field field : fields) {
            if (field.getExpression() == null) {
                field.setExpression(field.getId());
            }
            if (!field.getNoDisplay() && field.getSelectBody() == null) {
                field.setSelectBody(colon("expression"));
            }
            if (!field.getNoSorting() && field.getSortingBody() == null) {
                field.setSortingBody(colon("expression") + " " + colon(field.getId() + "Direction"));
            }
            for (N2oQuery.Filter filter : field.getFilterList()) {
                if (filter.getText() == null)
                    filter.setText(colon("expression") + " " + colon(filter.getType().name()) + " " + colon(filter.getFilterField()));
            }
        }
        return fields;
    }

}
