package net.n2oapp.framework.config.metadata.compile.query;


import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.data.validation.MandatoryValidation;
import net.n2oapp.framework.api.exception.SeverityType;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.dataprovider.N2oRestDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.query.*;
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

        List<SimpleField> simpleFields = source.getSimpleFields();
        compilePreFilters(source, p, context.getFilters());
        query.setDisplayValues(Collections.unmodifiableMap(initDisplayValues(simpleFields)));
        query.setDisplayFields(Collections.unmodifiableList(initDisplayFields(simpleFields)));
        query.setSortingFields(Collections.unmodifiableList(initSortingFields(simpleFields)));
        query.setFieldsMap(Collections.unmodifiableMap(initFieldsMap(simpleFields, query.getId())));
        query.setFieldNamesMap(Collections.unmodifiableMap(initFieldNamesMap(query.getFieldsMap())));
        query.setSortingSet(Collections.unmodifiableSet(initSortingSet(query.getSortingFields())));
        query.setFiltersMap(Collections.unmodifiableMap(initFiltersMap(source, query, p)));
        query.setInvertFiltersMap(Collections.unmodifiableMap(initInvertFiltersMap(source, query.getFieldsMap())));
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

    private Map<String, Map.Entry<String, FilterType>> initInvertFiltersMap(N2oQuery source, Map<String, SimpleField> fieldsMap) {
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

    private Set<String> initSortingSet(List<SimpleField> sortings) {
        return sortings.stream().map(SimpleField::getId).collect(Collectors.toSet());
    }

    private void initExpressions(CompiledQuery query) {
        List<String> select = new ArrayList<>();
        List<String> join = new ArrayList<>();
        query.getDisplayFields().forEach(f -> {
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
        query.getFieldsMap().values().stream().filter(queryField -> source.isSearchAvailable(queryField.getId())).forEach(queryField -> {
            Map<FilterType, N2oQuery.Filter> filters = new HashMap<>();
            for (N2oQuery.Filter f : source.getFiltersList(queryField.getId())) {
                if (f.getDomain() == null) {
                    f.setDomain(DomainProcessor.getDomain(queryField.getDomain(), f.getType()));
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
            result.put(queryField.getId(), filters);
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
            if (field instanceof SimpleField) {
                SimpleField simpleField = (SimpleField) field;
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

    private void initDefaultFields(List<AbstractField> fields, String parentMapping) {
        for (AbstractField field : fields) {
            if (field instanceof ReferenceField) {
                field.setMapping(concatMappings(castDefault(field.getMapping(), spel(field.getId())), parentMapping));
                initDefaultFields(Arrays.asList(((ReferenceField) field).getFields()),
                        field instanceof ListField ? null : field.getMapping());
            }
            else
                initDefaultSimpleField(((SimpleField) field), parentMapping);
        }
    }

    private void initDefaultSimpleField(SimpleField field, String parentMapping) {
        field.setName(castDefault(field.getName(), field.getId()));
        field.setNoDisplay(castDefault(field.getNoDisplay(), false));
        field.setNoSorting(castDefault(field.getNoSorting(), false));
        field.setNoJoin(castDefault(field.getNoJoin(), false));

        if (!field.getNoDisplay()) {
            if (field.getMapping() == null)
                field.setMapping(spel(field.getId()));
            field.setMapping(concatMappings(field.getMapping(), parentMapping));
        }
        if (!field.getNoSorting()) {
            if (field.getSortingMapping() == null)
                field.setSortingMapping(spel(field.getId() + "Direction"));
            field.setSortingMapping(concatMappings(field.getSortingMapping(), parentMapping));
        }
    }

    private String concatMappings(String child, String parent) {
        return spel(parent != null ? StringUtils.unwrapSpel(parent) + "." + StringUtils.unwrapSpel(parent) : StringUtils.unwrapSpel(child));
    }

    private void initDefaultExpression(List<AbstractField> fields) {
        for (AbstractField field : fields) {
            if (field instanceof SimpleField && ((SimpleField) field).getExpression() == null) {
                ((SimpleField) field).setExpression(field.getId());
            }
        }
    }

    public static Map<String, String> initDisplayValues(List<SimpleField> displayFields) {
        Map<String, String> displayValues = new HashMap<>();
        for (SimpleField field : displayFields) {
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

    private static List<SimpleField> initSortingFields(List<SimpleField> fields) {
        List<SimpleField> result = new ArrayList<>();
        for (SimpleField field : fields) {
            if (!field.getNoSorting()) {
                result.add(field);
            }
        }
        return result;
    }

    private static List<SimpleField> initDisplayFields(List<SimpleField> fields) {
        List<SimpleField> result = new ArrayList<>();
        for (SimpleField field : fields) {
            if (!field.getNoDisplay()) {
                result.add(field);
            }
        }
        return result;
    }

    private static Map<String, SimpleField> initFieldsMap(List<SimpleField> fields, String id) {
        Map<String, SimpleField> result = new StrictMap<>("Field '%s' in query '" + id + "' not found");
        for (SimpleField field : fields) {
            result.put(field.getId(), field);
        }
        return result;
    }


    private static Map<String, String> initFieldNamesMap(Map<String, SimpleField> fieldsMap) {
        return fieldsMap.values()
                .stream()
                .collect(Collectors.toMap(SimpleField::getId, SimpleField::getName));
    }


}
