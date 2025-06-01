package net.n2oapp.framework.config.metadata.compile.query;

import net.n2oapp.criteria.filters.FilterTypeEnum;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.data.validation.MandatoryValidation;
import net.n2oapp.framework.api.exception.SeverityTypeEnum;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.dataprovider.N2oRestDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.query.AbstractField;
import net.n2oapp.framework.api.metadata.global.dao.query.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.query.field.QueryReferenceField;
import net.n2oapp.framework.api.metadata.global.dao.query.field.QuerySimpleField;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oSwitch;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.Filter;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.metadata.compile.N2oCompileProcessor;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Component;

import java.util.*;

import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;
import static net.n2oapp.framework.api.DynamicUtil.isDynamic;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.spel;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.register.route.RouteUtil.normalize;
import static net.n2oapp.framework.config.register.route.RouteUtil.normalizeParam;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Компиляция query
 */
@Component
public class N2oQueryCompiler implements BaseSourceCompiler<CompiledQuery, N2oQuery, QueryContext> {

    private static final String IS_SELECTED = "n2o.api.query.field.is_selected";
    private static final String IS_SORTED = "n2o.api.query.field.is_sorted";
    private static final String FILTERS_SEPARATOR = "n2o.config.rest.filters_separator";
    private static final String SELECT_SEPARATOR = "n2o.config.rest.select_separator";
    private static final String JOIN_SEPARATOR = "n2o.config.rest.join_separator";
    private static final String SORTING_SEPARATOR = "n2o.config.rest.sorting_separator";

    @Override
    public Class<N2oQuery> getSourceClass() {
        return N2oQuery.class;
    }

    @Override
    public CompiledQuery compile(N2oQuery source, QueryContext context, CompileProcessor p) {
        source.initAbsoluteIds();
        CompiledQuery query = new CompiledQuery();
        String queryId = context.getSourceId((N2oCompileProcessor) p);
        query.setId(queryId);
        if (source.getObjectId() != null) {
            query.setObject(p.getCompiled(new ObjectContext(source.getObjectId())));
        }

        String route = normalize(castDefault(source.getRoute(), queryId));
        query.setRoute(isDynamic(route)
                ? route.replaceAll("[?=&]", "_")
                : route);
        query.setLists(initSeparators(source.getLists(), p));
        query.setUniques(initSeparators(source.getUniques(), p));
        query.setCounts(initSeparators(source.getCounts(), p));
        if (!CollectionUtils.isEmpty(context.getValidations()))
            query.setValidations(context.getValidations());
        List<AbstractField> fields = source.getFields() != null
                ? Arrays.asList(source.getFields())
                : List.of();
        initDefaultFilters(source.getFilters());
        initDefaultFields(
                fields,
                p.resolve(property((IS_SELECTED)), Boolean.class),
                p.resolve(property((IS_SORTED)), Boolean.class),
                p
        );

        compilePreFilters(source, p, context.getFilters());
        query.setDisplayFields(unmodifiableList(initDisplayFields(fields)));
        List<QuerySimpleField> simpleFields = source.getSimpleFields();
        query.setSortingFields(unmodifiableList(initSortingFields(simpleFields)));
        query.setFieldsMap(unmodifiableMap(initFieldsMap(fields, query.getId())));
        query.setSimpleFieldsMap(unmodifiableMap(initSimpleFieldsMap(simpleFields)));
        query.setFiltersMap(unmodifiableMap(initFiltersMap(source, query, p)));
        query.setInvertFiltersMap(unmodifiableMap(initInvertFiltersMap(source, query.getFieldsMap())));
        query.setFilterFieldsMap(unmodifiableMap(initFilterFieldsMap(query.getFiltersMap())));
        query.setParamToFilterIdMap(unmodifiableMap(initParamToFilterIdMap(query.getFilterFieldsMap())));
        query.setFilterIdToParamMap(unmodifiableMap(initFilterIdToParamMap(query.getParamToFilterIdMap())));
        query.setSubModelQueries(context.getSubModelQueries());
        initExpressions(query);
        query.setProperties(p.mapAttributes(source));
        query.setCopiedFields(context.getCopiedFields());

        return query;
    }

    private static List<AbstractField> initDisplayFields(List<AbstractField> fields) {
        return fields.stream()
                .filter(AbstractField::getIsSelected)
                .toList();
    }

    private static Map<String, AbstractField> initFieldsMap(List<AbstractField> fields, String id) {
        Map<String, AbstractField> result = new HashMap<>();
        for (AbstractField field : fields) {
            result.put(field.getAbsoluteId(), field);
            if (field instanceof QueryReferenceField referenceField)
                result.putAll(initFieldsMap(Arrays.asList(referenceField.getFields()), id));
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
        if (preFilters == null || source.getFilters() == null)
            return;
        for (Filter preFilter : preFilters) {
            for (N2oQuery.Filter filter : source.getFilters()) {
                if (filter.getFilterId().equals(preFilter.getFilterId())) {
                    filter.setParam(castDefault(preFilter.getParam(), filter.getParam()));
                    if (preFilter.getLink() != null && !preFilter.getLink().isLink()) {
                        filter.setCompiledDefaultValue(
                                castDefault(
                                        preFilter.getLink().getValue(),
                                        () -> p.resolve(filter.getDefaultValue(), filter.getDomain())
                                )
                        );
                    }
                }
            }
        }
    }

    /**
     * Поиск фильтров полей выборки, путем фильтрации тех полей, ключи которых соответствуют ключам фильтров.
     *
     * @param source    исходная модель выборки
     * @param fieldsMap скомпилированные поля выборки
     * @return фильтры
     */
    private Map<String, Map.Entry<String, FilterTypeEnum>> initInvertFiltersMap(N2oQuery source, Map<String, AbstractField> fieldsMap) {
        Map<String, Map.Entry<String, FilterTypeEnum>> invertFiltersMap = new HashMap<>();
        fieldsMap.keySet()
                .stream()
                .filter(source::isSearchAvailable)
                .map(fieldsMap::get)
                .forEach(queryField -> {
                    for (N2oQuery.Filter f : source.getFiltersList(queryField.getAbsoluteId())) {
                        invertFiltersMap.put(
                                f.getFilterId(),
                                new CompiledQuery.FilterEntry(queryField.getAbsoluteId(), f.getType())
                        );
                    }
                });

        return invertFiltersMap;
    }

    private Map<String, String> initParamToFilterIdMap(Map<String, N2oQuery.Filter> filterIdsMap) {
        Map<String, String> filterParams = new HashMap<>();
        for (N2oQuery.Filter filter : filterIdsMap.values()) {
            String param = castDefault(filter.getParam(), () -> normalizeParam(filter.getFilterId()));
            filterParams.put(param, filter.getFilterId());
        }

        return filterParams;
    }

    private Map<String, String> initFilterIdToParamMap(Map<String, String> paramToFilterMap) {
        Map<String, String> filterParams = new HashMap<>();
        for (Map.Entry<String, String> paramEntry : paramToFilterMap.entrySet()) {
            filterParams.put(paramEntry.getValue(), paramEntry.getKey());
        }

        return filterParams;
    }

    private Map<String, N2oQuery.Filter> initFilterFieldsMap(Map<String, Map<FilterTypeEnum, N2oQuery.Filter>> filtersMap) {
        Map<String, N2oQuery.Filter> result = new HashMap<>();
        for (Map<FilterTypeEnum, N2oQuery.Filter> filterMap : filtersMap.values()) {
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
                .toList());
    }

    private N2oQuery.Selection[] initSeparators(N2oQuery.Selection[] selections, CompileProcessor p) {
        if (selections == null)
            return null;

        for (N2oQuery.Selection selection : selections) {
            if (selection.getInvocation() instanceof N2oRestDataProvider restDataProvider) {
                restDataProvider.setFiltersSeparator(castDefault(restDataProvider.getFiltersSeparator(),
                        () -> p.resolve(property(FILTERS_SEPARATOR), String.class)));
                restDataProvider.setSelectSeparator(castDefault(restDataProvider.getSelectSeparator(),
                        () -> p.resolve(property(SELECT_SEPARATOR), String.class)));
                restDataProvider.setJoinSeparator(castDefault(restDataProvider.getJoinSeparator(),
                        () -> p.resolve(property(JOIN_SEPARATOR), String.class)));
                restDataProvider.setSortingSeparator(castDefault(restDataProvider.getSortingSeparator(),
                        () -> p.resolve(property(SORTING_SEPARATOR), String.class)));
            }
        }

        return selections;
    }

    private Map<String, Map<FilterTypeEnum, N2oQuery.Filter>> initFiltersMap(N2oQuery source, CompiledQuery query, CompileProcessor p) {
        Map<String, Map<FilterTypeEnum, N2oQuery.Filter>> result = new HashMap<>();
        query.getFieldsMap()
                .keySet()
                .stream()
                .filter(source::isSearchAvailable)
                .forEach(key -> {
                    AbstractField queryField = query.getFieldsMap().get(key);
                    Map<FilterTypeEnum, N2oQuery.Filter> filters = new EnumMap<>(FilterTypeEnum.class);
                    for (N2oQuery.Filter f : source.getFiltersList(queryField.getAbsoluteId())) {
                        if (f.getDomain() == null && queryField instanceof QuerySimpleField simpleField) {
                            f.setDomain(DomainProcessor.getDomain(simpleField.getDomain(), f.getType()));
                        }
                        if (f.getCompiledDefaultValue() == null && f.getDefaultValue() != null) {
                            f.setCompiledDefaultValue(p.resolve(f.getDefaultValue(), f.getDomain()));
                        }
                        filters.put(f.getType(), f);
                        initValidation(query, p, f);
                    }
                    result.put(queryField.getAbsoluteId(), filters);
                });

        return result;
    }

    private void initValidation(CompiledQuery query, CompileProcessor p, N2oQuery.Filter f) {
        if (f.getRequired() != null && f.getRequired()) {
            MandatoryValidation mandatory = new MandatoryValidation(
                    f.getFilterId(),
                    p.getMessage("n2o.required.filter"),
                    f.getFilterId()
            );
            mandatory.setMoment(N2oValidation.ServerMomentEnum.BEFORE_QUERY);
            mandatory.setSeverity(SeverityTypeEnum.DANGER);

            if (query.getValidations() == null)
                query.setValidations(new ArrayList<>());
            query.getValidations().add(mandatory);
        }
    }

    private void initDefaultFilters(N2oQuery.Filter[] filters) {
        if (filters == null)
            return;

        for (N2oQuery.Filter filter : filters) {
            filter.setFilterId(
                    castDefault(
                            filter.getFilterId(),
                            () -> normalizeParam(filter.getFieldId()) + "_" + filter.getType()
                    )
            );
            filter.setParam(
                    castDefault(
                            filter.getParam(),
                            () -> normalizeParam(filter.getFilterId())
                    )
            );
            if (filter.getText() != null)
                filter.setText(filter.getText().trim());
            if (filter.getMapping() == null)
                filter.setMapping(spel(filter.getFilterId()));
        }
    }

    private void initDefaultFields(List<AbstractField> fields, Boolean defaultSelected,
                                   Boolean defaultSorted, CompileProcessor p) {
        for (AbstractField field : fields) {
            field.setIsSelected(castDefault(field.getIsSelected(), defaultSelected));

            if (field.getIsSelected()) {
                field.setMapping(castDefault(field.getMapping(), () -> spel(field.getId())));
            }
            if (field instanceof QueryReferenceField referenceField) {
                field.setMapping(castDefault(field.getMapping(), () -> spel(field.getId())));
                initDefaultFields(Arrays.asList(referenceField.getFields()), defaultSelected, defaultSorted, p);
            } else {
                initDefaultSimpleField(((QuerySimpleField) field), defaultSorted, p);
            }
        }
    }

    private void initDefaultSimpleField(QuerySimpleField field, Boolean defaultSorted, CompileProcessor p) {
        field.setName(
                castDefault(field.getName(), field.getId())
        );
        field.setIsSorted(
                castDefault(field.getIsSorted(), !isBlank(field.getSortingExpression()), defaultSorted)
        );

        if (field.getIsSorted()) {
            field.setSortingMapping(
                    castDefault(field.getSortingMapping(), () -> spel(field.getId() + "Direction"))
            );
        }
        compileSwitch(field, p);
    }

    private void compileSwitch(QuerySimpleField field, CompileProcessor p) {
        N2oSwitch n2oSwitch = field.getN2oSwitch();
        if (n2oSwitch == null)
            return;

        field.getN2oSwitch().setValueFieldId(field.getId());
        Map<Object, String> resolvedCases = new HashMap<>();
        if (!MapUtils.isEmpty(n2oSwitch.getCases())) {
            for (String key : n2oSwitch.getCases().keySet()) {
                resolvedCases.put(
                        p.resolve(key),
                        n2oSwitch.getCases().get(key)
                );
            }
        }
        n2oSwitch.setResolvedCases(resolvedCases);
    }

    private static List<QuerySimpleField> initSortingFields(List<QuerySimpleField> fields) {
        List<QuerySimpleField> result = new ArrayList<>();
        for (QuerySimpleField field : fields)
            if (field.getIsSorted())
                result.add(field);

        return result;
    }

    private static Map<String, QuerySimpleField> initSimpleFieldsMap(List<QuerySimpleField> fields) {
        Map<String, QuerySimpleField> result = new HashMap<>();
        for (QuerySimpleField field : fields)
            result.put(field.getAbsoluteId(), field);

        return result;
    }
}
