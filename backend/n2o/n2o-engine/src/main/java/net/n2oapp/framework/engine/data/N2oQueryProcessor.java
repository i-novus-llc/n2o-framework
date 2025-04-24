package net.n2oapp.framework.engine.data;

import lombok.Setter;
import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.api.Sorting;
import net.n2oapp.criteria.api.SortingDirection;
import net.n2oapp.criteria.dataset.DataList;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.filters.Filter;
import net.n2oapp.criteria.filters.FilterReducer;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.criteria.filters.Result;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.context.ContextProcessor;
import net.n2oapp.framework.api.criteria.N2oPreparedCriteria;
import net.n2oapp.framework.api.criteria.Restriction;
import net.n2oapp.framework.api.data.*;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.aware.MetadataEnvironmentAware;
import net.n2oapp.framework.api.metadata.global.dao.invocation.N2oArgumentsInvocation;
import net.n2oapp.framework.api.metadata.global.dao.query.AbstractField;
import net.n2oapp.framework.api.metadata.global.dao.query.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.query.field.QueryListField;
import net.n2oapp.framework.api.metadata.global.dao.query.field.QueryReferenceField;
import net.n2oapp.framework.api.metadata.global.dao.query.field.QuerySimpleField;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.engine.exception.N2oFoundMoreThanOneRecordException;
import net.n2oapp.framework.engine.exception.N2oRecordNotFoundException;
import net.n2oapp.framework.engine.exception.N2oSpelException;
import net.n2oapp.framework.engine.exception.N2oUniqueRequestNotFoundException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.*;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static net.n2oapp.framework.engine.util.ArgumentsInvocationUtil.mapToArgs;
import static net.n2oapp.framework.engine.util.MappingProcessor.*;

/**
 * Процессор выборок
 */
public class N2oQueryProcessor implements QueryProcessor, MetadataEnvironmentAware, ApplicationContextAware {
    private static final ExpressionParser parser = new SpelExpressionParser();

    private ContextProcessor contextProcessor;
    private final N2oInvocationFactory invocationFactory;
    @Setter
    private CriteriaConstructor criteriaConstructor = new N2oCriteriaConstructor(false);
    private DomainProcessor domainProcessor;
    private final QueryExceptionHandler exceptionHandler;
    @Setter
    private ApplicationContext applicationContext;

    @Setter
    private boolean pageStartsWith0;
    @Setter
    private String ascExpression;
    @Setter
    private String descExpression;

    public N2oQueryProcessor(N2oInvocationFactory invocationFactory,
                             QueryExceptionHandler exceptionHandler) {
        this.invocationFactory = invocationFactory;
        this.exceptionHandler = exceptionHandler;
    }

    @SuppressWarnings("unchecked")
    public CollectionPage<DataSet> execute(final CompiledQuery query, final N2oPreparedCriteria criteria) {
        if (criteria.getSize() == 1) {
            return executeOneSizeQuery(query, criteria);
        }
        return executePageQuery(findListSelection(query, criteria), query, criteria);
    }

    @SuppressWarnings("unchecked")
    public Integer executeCount(final CompiledQuery query, final N2oPreparedCriteria criteria) {
        resolveRestriction(query, criteria);
        addDefaultFilters(query, criteria);
        N2oQuery.Selection selection = findCountSelection(query, criteria);
        ActionInvocationEngine engine = invocationFactory.produce(selection.getInvocation().getClass());
        Object result;
        if (engine instanceof ArgumentsInvocationEngine argsInvocationEngine) {
            try {
                result = argsInvocationEngine.invoke(
                        (N2oArgumentsInvocation) selection.getInvocation(),
                        mapToArgs((N2oArgumentsInvocation) selection.getInvocation(),
                                query, criteria, criteriaConstructor, domainProcessor));
            } catch (Exception e) {
                throw exceptionHandler.handle(query, criteria, e);
            }
        } else {
            Map<String, Object> map = new LinkedHashMap<>();
            prepareMapForQuery(map, selection, query, criteria);
            try {
                result = engine.invoke(selection.getInvocation(), map);
            } catch (Exception e) {
                throw exceptionHandler.handle(query, criteria, e);
            }
        }
        return calculateCount(result, selection.getCountMapping());
    }

    private Integer calculateCount(Object result, String countMapping) {
        try {
            return outMap(result, countMapping, Integer.class);
        } catch (N2oException e) {
            throw new N2oSpelException(countMapping, e);
        }
    }

    public CollectionPage<DataSet> executeOneSizeQuery(CompiledQuery query, N2oPreparedCriteria criteria) {
        N2oQuery.Selection selection = findUniqueSelection(query, criteria);
        if (selection.getType().equals(N2oQuery.Selection.Type.unique)) {
            criteria.setSize(2);
            criteria.setCount(2);
        }
        Object result = executeQuery(selection, query, criteria);
        criteria.setSize(1);
        if (selection.getType().equals(N2oQuery.Selection.Type.list)) {
            CollectionPage<DataSet> page = preparePageResult(result, query, selection, criteria);
            if (CollectionUtils.isEmpty(page.getCollection())) {
                throw new N2oRecordNotFoundException();
            }
            if (page.getCollection().size() != 1) {
                throw new N2oFoundMoreThanOneRecordException();
            }
            return page;
        } else if (selection.getType().equals(N2oQuery.Selection.Type.unique)) {
            DataSet single = prepareSingleResult(result, query, selection);
            if (single.isEmpty()) {
                throw new N2oRecordNotFoundException();
            }

            return new CollectionPage<>(1, Collections.singletonList(single), criteria);
        } else
            throw new UnsupportedOperationException();
    }

    private N2oQuery.Selection findCountSelection(final CompiledQuery query, final N2oPreparedCriteria criteria) {
        Set<String> filterFields = getFilterIds(query, criteria);
        N2oQuery.Selection selection = chooseSelection(query.getCounts(), filterFields, query.getId());
        if (selection == null)
            throw new N2oException(String.format("В %s.query.xml не найден <count> запрос необходимый для пагинации", query.getId()));
        return selection;
    }


    private N2oQuery.Selection findUniqueSelection(final CompiledQuery query, final N2oPreparedCriteria criteria) {
        resolveRestriction(query, criteria);
        addDefaultFilters(query, criteria);
        Set<String> filterFields = getFilterIds(query, criteria);
        N2oQuery.Selection selection = chooseSelection(query.getUniques(), filterFields, query.getId());
        if (selection != null)
            return selection;
        selection = chooseSelection(query.getLists(), filterFields, query.getId());
        if (selection == null)
            throw new N2oUniqueRequestNotFoundException(query.getId());
        return selection;
    }

    private N2oQuery.Selection findListSelection(final CompiledQuery query, final N2oPreparedCriteria criteria) {
        resolveRestriction(query, criteria);
        addDefaultFilters(query, criteria);
        Set<String> filterFields = getFilterIds(query, criteria);
        N2oQuery.Selection selection = chooseSelection(query.getLists(), filterFields, query.getId());
        if (selection == null)
            throw new N2oException(String.format("В %s.query.xml не найден <list> запрос", query.getId()));
        return selection;
    }

    private Set<String> getFilterIds(CompiledQuery query, N2oPreparedCriteria criteria) {
        return criteria.getRestrictions() == null ? Collections.emptySet() :
                criteria.getRestrictions().stream()
                        .map(r -> query.getFilterFieldId(r.getFieldId(), r.getType()))
                        .collect(Collectors.toSet());
    }

    @SuppressWarnings("unchecked")
    private Object executeQuery(N2oQuery.Selection selection, CompiledQuery query, N2oPreparedCriteria criteria) {
        ActionInvocationEngine engine = invocationFactory.produce(selection.getInvocation().getClass());
        Object result;
        if (engine instanceof ArgumentsInvocationEngine argsInvocationEngine) {
            try {
                result = argsInvocationEngine.invoke(
                        (N2oArgumentsInvocation) selection.getInvocation(),
                        mapToArgs((N2oArgumentsInvocation) selection.getInvocation(),
                                query, criteria, criteriaConstructor, domainProcessor)
                );
            } catch (Exception e) {
                throw exceptionHandler.handle(query, criteria, e);
            }
        } else if (engine instanceof MapInvocationEngine) {
            Map<String, Object> map = new LinkedHashMap<>();
            prepareMapForQuery(map, selection, query, criteria);
            prepareMapForPage(map, criteria, pageStartsWith0);
            try {
                result = engine.invoke(selection.getInvocation(), map);
            } catch (Exception e) {
                throw exceptionHandler.handle(query, criteria, e);
            }
        } else
            throw new UnsupportedOperationException("InvocationEngine должен быть наследован от `ArgumentsInvocationEngine` или `MapInvocationEngine`");

        return result;
    }

    private CollectionPage<DataSet> executePageQuery(N2oQuery.Selection selection, CompiledQuery query, N2oPreparedCriteria criteria) {
        if (criteria != null && criteria.getRestrictions() != null) {
            Set<String> restrictionFieldIds = criteria.getRestrictions().stream().map(Restriction::getFieldId).collect(Collectors.toSet());
            for (String fieldId : restrictionFieldIds) {
                if (reduceFiltersByField(criteria, fieldId))
                    return new CollectionPage<>(0, Collections.emptyList(), criteria);
            }
        }
        Object result = executeQuery(selection, query, criteria);
        CollectionPage<DataSet> page = preparePageResult(result, query, selection, criteria);
        addIdIfNotPresent(query, page);
        return page;
    }

    private boolean reduceFiltersByField(N2oPreparedCriteria criteria, String fieldId) {
        List<Restriction> restrictionsByField = criteria.getRestrictions(fieldId);
        if (restrictionsByField.size() > 1) {
            List<Filter> resFilters = new ArrayList<>();
            resFilters.add(restrictionsByField.get(0));
            for (int i = 1; i < restrictionsByField.size(); i++) {
                boolean notMergeable = false;
                for (Filter result : resFilters) {
                    Result reduceResult = FilterReducer.reduce(result, restrictionsByField.get(i));
                    if (reduceResult.isSuccess()) {
                        resFilters.remove(result);
                        resFilters.add(reduceResult.getResultFilter());
                        notMergeable = false;
                        break;
                    } else if (reduceResult.getType().equals(Result.Type.notMergeable)) {
                        notMergeable = true;
                    } else {
                        return true;
                    }
                }
                if (notMergeable) {
                    resFilters.add(restrictionsByField.get(i));
                }
            }
            criteria.removeFilterForField(fieldId);
            resFilters.forEach(result -> criteria.addRestriction(new Restriction(fieldId, result)));
        }
        return false;
    }

    private void addDefaultFilters(CompiledQuery query, N2oPreparedCriteria criteria) {
        for (Map.Entry<String, Map<FilterType, N2oQuery.Filter>> entry : query.getFiltersMap().entrySet()) {
            for (N2oQuery.Filter filter : entry.getValue().values()) {
                if (filter.getCompiledDefaultValue() != null && !criteria.containsRestriction(entry.getKey())) {
                    Object value = prepareFilterValue(filter.getCompiledDefaultValue(), filter, null);
                    if (value != null) {
                        Restriction defaultRestriction = new Restriction(entry.getKey(), value, filter.getType());
                        criteria.addRestriction(defaultRestriction);
                    }
                }
            }
        }
    }

    public void prepareMapForQuery(Map<String, Object> map, N2oQuery.Selection selection, CompiledQuery query, N2oPreparedCriteria criteria) {
        map.put("select", query.getSelectExpressions());
        prepareSelectKeys(map, query.getDisplayFields(), query);

        List<String> where = new ArrayList<>();
        for (Restriction r : criteria.getRestrictions()) {
            N2oQuery.Filter filter = query.getFiltersMap().get(r.getFieldId()).get(r.getType());
            if (filter == null)
                throw new N2oUniqueRequestNotFoundException(query.getId());
            if (filter.getText() != null)
                where.add(filter.getText());
            inMap(map, r.getFieldId(), filter.getMapping(), r.getValue());
        }
        map.put("filters", where);

        List<String> sortingExp = new ArrayList<>();
        if (criteria.getSorting() != null)
            for (Sorting sorting : criteria.getSortings()) {
                QuerySimpleField field = query.getSimpleFieldsMap().get(sorting.getField());
                if (Boolean.FALSE.equals(field.getIsSorted()))
                    continue;
                sortingExp.add(field.getSortingExpression());
                inMap(map, field.getId(), field.getSortingMapping(), getSortingDirectionExpression(sorting.getDirection(), selection));
            }
        map.put("sorting", sortingExp);

        if (criteria.getAdditionalFields() != null) {
            criteria.getAdditionalFields().entrySet().stream().filter(es -> nonNull(es.getValue()))
                    .forEach(es -> map.put(es.getKey(), es.getValue()));
        }
    }

    private String getSortingDirectionExpression(SortingDirection direction, N2oQuery.Selection selection) {
        if (direction == SortingDirection.ASC)
            return Objects.requireNonNullElse(selection.getAscExpression(), ascExpression);
        else
            return Objects.requireNonNullElse(selection.getDescExpression(), descExpression);
    }

    private void prepareSelectKeys(Map<String, Object> map, List<AbstractField> fields, CompiledQuery query) {
        for (AbstractField field : fields) {
            if (field instanceof QueryReferenceField referenceField) {
                if (referenceField.getSelectKey() != null) {
                    List<AbstractField> displayedInnerFields = query.getDisplayedInnerFields(referenceField);
                    map.put(referenceField.getSelectKey(),
                            displayedInnerFields.stream().map(AbstractField::getSelectExpression).collect(Collectors.toList()));
                    prepareSelectKeys(map, displayedInnerFields, query);
                }
            }
        }
    }

    public void prepareMapForPage(Map<String, Object> map, N2oPreparedCriteria criteria, boolean pageStartsWith0) {
        map.put("limit", criteria.getSize());
        map.put("offset", criteria.getFirst());
        if (criteria.getCount() != null)
            map.put("count", criteria.getCount());
        map.put("page", pageStartsWith0 ? criteria.getPage() - 1 : criteria.getPage());
    }

    private Object prepareFilterValue(Object value, N2oQuery.Filter filter, DataSet data) {
        Object result = value;
        result = contextProcessor.resolve(result);
        result = domainProcessor.deserialize(result, filter == null ? null : filter.getDomain());
        result = normalizeValue(result, filter == null ? null : filter.getNormalize(), data, parser, applicationContext);
        return result;
    }

    private DataSet prepareSingleResult(Object res, CompiledQuery query,
                                        N2oQuery.Selection selection) {
        Object result;
        try {
            result = outMap(res, selection.getResultMapping(), Object.class);
        } catch (N2oException e) {
            throw new N2oRecordNotFoundException(e);
        }

        result = normalizeValue(result, selection.getResultNormalize(), null, parser, applicationContext);
        return mapFields(result, query.getDisplayFields(), null);
    }

    private CollectionPage<DataSet> preparePageResult(Object res, CompiledQuery query, N2oQuery.Selection selection,
                                                      N2oPreparedCriteria criteria) {
        Object additionalInfo = null;
        if (selection.getAdditionalMapping() != null)
            additionalInfo = outMap(res, selection.getAdditionalMapping(), Object.class);

        Collection<?> result = outMap(res, selection.getResultMapping(), Collection.class);
        try {
            result = (Collection<?>) normalizeValue(
                    result, selection.getResultNormalize(), null, parser, applicationContext);
        } catch (ClassCastException e) {
            throw new N2oException("В результате нормализации через `result-normalize` получилась не коллекция");
        }

        List<DataSet> content = result.stream()
                .map(obj -> mapFields(obj, query.getDisplayFields(), null))
                .collect(Collectors.toList());

        CollectionPage<DataSet> collectionPage;

        if (Boolean.FALSE.equals(criteria.getWithCount())) {
            Boolean hasNext = getNext(content, criteria, selection, query);
            collectionPage = new CollectionPage<>(hasNext, content, criteria);
        } else {
            int size = getSize(content, criteria, () -> {
                if (criteria.getSize() == 1) {
                    return 1;
                } else if (selection.getCountMapping() == null) {
                    return executeCount(query, criteria);
                } else {
                    return calculateCount(res, selection.getCountMapping());
                }
            });
            collectionPage = new CollectionPage<>(size, content, criteria);
        }
        collectionPage.setAdditionalInfo(additionalInfo);
        return collectionPage;
    }

    private N2oQuery.Selection chooseSelection(N2oQuery.Selection[] selections, Set<String> filterFields, String queryId) {
        if (selections == null)
            return null;

        if (filterFields == null) {
            N2oQuery.Selection result = findBaseSelection(selections);
            if (result == null) {
                throw new N2oException(String.format("В %s.query.xml не найден запрос без фильтров", queryId));
            }
            return result;
        }
        return findSelectionByFilters(selections, filterFields);
    }

    private N2oQuery.Selection findBaseSelection(N2oQuery.Selection[] lists) {
        for (N2oQuery.Selection selection : lists)
            if (ArrayUtils.isEmpty(selection.getFilters()))
                return selection;
        return null;
    }

    private N2oQuery.Selection findSelectionByFilters(N2oQuery.Selection[] selections, Set<String> filterFields) {
        for (N2oQuery.Selection selection : selections) {
            if (ArrayUtils.isEmpty(selection.getFilters()))
                continue;

            Set<String> filters = Arrays.stream(selection.getFilters()).collect(Collectors.toSet());
            if (filters.equals(filterFields))
                return selection;
        }
        return findBaseSelection(selections);
    }

    /**
     * Приведение значений фильтраций к домену и контексту
     */
    private void resolveRestriction(CompiledQuery query, N2oPreparedCriteria criteria) {
        Set<String> restrictionsForRemove = null;
        DataSet data = new DataSet();
        for (Restriction restriction : criteria.getRestrictions()) {
            data.put(restriction.getFieldId(), restriction.getValue());
        }

        for (Restriction restriction : criteria.getRestrictions()) {
            N2oQuery.Filter filter = query.getFiltersMap().get(restriction.getFieldId()).get(restriction.getType());
            Object value = prepareFilterValue(restriction.getValue(), filter, data);
            if (value != null) {
                restriction.setValue(value);
            } else if (FilterType.Arity.nullary == restriction.getType().arity) {
                restriction.setValue(Boolean.TRUE);
            } else {
                //удаляем фильтрацию, если в результате резолва контекста значение по умолчанию стало null
                if (restrictionsForRemove == null)
                    restrictionsForRemove = new HashSet<>();
                restrictionsForRemove.add(restriction.getFieldId());
            }
        }
        if (restrictionsForRemove != null) {
            restrictionsForRemove.forEach(criteria::removeFilterForField);
        }
    }

    private void addIdIfNotPresent(CompiledQuery query, CollectionPage<DataSet> collectionPage) {
        if (!query.getSimpleFieldsMap().containsKey(QuerySimpleField.PK))
            return;
        if (Boolean.TRUE.equals(query.getFieldsMap().get(QuerySimpleField.PK).getIsSelected()))
            return;
        int i = 1;
        for (DataSet dataSet : collectionPage.getCollection()) {
            dataSet.put(QuerySimpleField.PK, i++);
        }
    }

    private DataSet mapFields(Object entry, List<AbstractField> fields, DataSet parentData) {
        DataSet resultDataSet = new DataSet();
        fields.forEach(field -> mapField(field, resultDataSet, entry));
        fields.forEach(field -> normalizeField(field, resultDataSet, parentData));
        fields.forEach(field -> processInnerFields(field, resultDataSet, resultDataSet));
        fields.stream()
                .filter(
                        field -> field instanceof QuerySimpleField querySimpleField && querySimpleField.getN2oSwitch() != null)
                .forEach(
                        field -> applySwitch(resultDataSet, (QuerySimpleField) field)
                );
        return resultDataSet;
    }

    private void applySwitch(DataSet resultDataSet, QuerySimpleField field) {
        String valueFieldId = field.getN2oSwitch().getValueFieldId();
        String expression = ScriptProcessor.buildSwitchExpression(field.getN2oSwitch())
                .replace("`", "")
                .replace(valueFieldId, String.format("#this.get('%s')", valueFieldId));
        Object resultSwitch = normalizeValue(resultDataSet, expression, null, parser, applicationContext);
        resultDataSet.replace(valueFieldId, resultSwitch);
    }

    private void mapField(AbstractField field, DataSet target, Object entry) {
        if (field instanceof QueryReferenceField)
            outMap(target, entry, field.getId(), field.getMapping(), null, contextProcessor);
        else
            outMap(target, entry, field.getId(), field.getMapping(), ((QuerySimpleField) field).getDefaultValue(), contextProcessor);
    }

    private void processInnerFields(AbstractField field, DataSet target, DataSet parentData) {
        if (field instanceof QueryReferenceField refField) {
            if (field instanceof QueryListField queryField && target.getList(field.getId()) != null) {
                DataList list = new DataList(target.getList(field.getId()));
                for (int i = 0; i < list.size(); i++)
                    list.set(i, mapFields(target.getList(field.getId()).get(i), Arrays.asList(queryField.getFields()), parentData));
                target.put(field.getId(), list);
            } else if (target.get(field.getId()) != null)
                target.put(field.getId(), mapFields(target.get(field.getId()), Arrays.asList(refField.getFields()), parentData));
        }
    }

    private void normalizeField(AbstractField field, DataSet resultDataSet, DataSet parentDataSet) {
        if (field.getNormalize() != null) {
            Object obj = resultDataSet.get(field.getId());
            obj = contextProcessor.resolve(obj);
            try {
                resultDataSet.put(field.getId(), normalizeValue(obj, field.getNormalize(), resultDataSet, parentDataSet, parser, applicationContext));
            } catch (N2oSpelException e) {
                e.setFieldId(field.getId());
                throw e;
            }
        }
    }

    private Integer getSize(Collection<DataSet> content, N2oPreparedCriteria criteria,
                            IntSupplier totalSupplier) {
        int size;
        if (criteria.getFirst() == 0) {
            if (criteria.getSize() > content.size()) {
                size = content.size();
            } else {
                size = totalSupplier.getAsInt();
            }
        } else if (!content.isEmpty() && criteria.getSize() > content.size()) {
            size = criteria.getFirst() + content.size();
        } else {
            size = totalSupplier.getAsInt();
        }
        return size;
    }

    private Boolean getNext(Collection<DataSet> content, N2oPreparedCriteria criteria,
                            N2oQuery.Selection selection, CompiledQuery query) {
        if (criteria.getSize() <= content.size()) {
            N2oPreparedCriteria nextCriteria = new N2oPreparedCriteria(criteria);
            nextCriteria.setPage(nextCriteria.getPage() + 1);
            Object res = executeQuery(selection, query, nextCriteria);
            Collection<?> result = outMap(res, selection.getResultMapping(), Collection.class);
            try {
                result = (Collection<?>) normalizeValue(
                        result, selection.getResultNormalize(), null, parser, applicationContext);
            } catch (ClassCastException e) {
                throw new N2oException("В результате нормализации через `result-normalize` получилась не коллекция");
            }
            return !result.isEmpty();
        }
        return false;
    }

    @Override
    public void setEnvironment(MetadataEnvironment environment) {
        this.contextProcessor = environment.getContextProcessor();
        this.domainProcessor = environment.getDomainProcessor();
    }
}
