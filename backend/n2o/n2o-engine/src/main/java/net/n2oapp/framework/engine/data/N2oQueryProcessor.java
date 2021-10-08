package net.n2oapp.framework.engine.data;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.api.Sorting;
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
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oArgumentsInvocation;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.engine.exception.N2oFoundMoreThanOneRecordException;
import net.n2oapp.framework.engine.exception.N2oRecordNotFoundException;
import net.n2oapp.framework.engine.util.InvocationParametersMapping;
import net.n2oapp.framework.engine.exception.N2oUniqueRequestNotFoundException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static net.n2oapp.framework.engine.util.ArgumentsInvocationUtil.mapToArgs;
import static net.n2oapp.framework.engine.util.MappingProcessor.*;

/**
 * Процессор выборок
 */
public class N2oQueryProcessor implements QueryProcessor, MetadataEnvironmentAware, ApplicationContextAware {
    private static final ExpressionParser parser = new SpelExpressionParser();

    private ContextProcessor contextProcessor;
    private N2oInvocationFactory invocationFactory;
    private CriteriaConstructor criteriaConstructor = new N2oCriteriaConstructor(false);
    private DomainProcessor domainProcessor;
    private QueryExceptionHandler exceptionHandler;
    private ApplicationContext applicationContext;
    private boolean pageStartsWith0;

    public N2oQueryProcessor(N2oInvocationFactory invocationFactory,
                             QueryExceptionHandler exceptionHandler) {
        this.invocationFactory = invocationFactory;
        this.exceptionHandler = exceptionHandler;
    }

    @SuppressWarnings("unchecked")
    public CollectionPage<DataSet> execute(final CompiledQuery query, final N2oPreparedCriteria criteria) {
        if (criteria.getSize() == 1) {
            //todo сейчас size==1 это дефакто выборки "byId" и они брасают исключение, если не найдуит записей, что не очень удобно, когда нам надо просто 0 или 1 запись
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
        if (engine instanceof ArgumentsInvocationEngine) {
            try {
                result = ((ArgumentsInvocationEngine) engine).invoke(
                        (N2oArgumentsInvocation) selection.getInvocation(),
                        mapToArgs((N2oArgumentsInvocation) selection.getInvocation(),
                                query, criteria, criteriaConstructor, domainProcessor));
            } catch (Exception e) {
                throw exceptionHandler.handle(query, criteria, e);
            }
        } else {
            Map<String, Object> map = new LinkedHashMap<>();
            prepareMapForQuery(map, query, criteria);
            try {
                result = engine.invoke(selection.getInvocation(), map);
            } catch (Exception e) {
                throw exceptionHandler.handle(query, criteria, e);
            }
        }
        return outMap(result, selection.getCountMapping(), Integer.class);
    }


    public CollectionPage<DataSet> executeOneSizeQuery(CompiledQuery query, N2oPreparedCriteria criteria) {
        criteria.setSize(2);
        criteria.setCount(2);
        N2oQuery.Selection selection = findUniqueSelection(query, criteria);
        Object result = executeQuery(selection, query, criteria);
        criteria.setSize(1);
        if (selection.getType().equals(N2oQuery.Selection.Type.list)) {
            CollectionPage<DataSet> page = preparePageResult(result, query, selection, criteria);
            if (page.getCollection() == null || page.getCollection().size() == 0) {
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
            throw new N2oException(String.format("В %s.query.xml не найден <count> запрос необходимый для паджинации", query.getId()));
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
        if (engine instanceof ArgumentsInvocationEngine) {
            try {
                result = ((ArgumentsInvocationEngine) engine).invoke(
                        (N2oArgumentsInvocation) selection.getInvocation(),
                        mapToArgs((N2oArgumentsInvocation) selection.getInvocation(),
                                query, criteria, criteriaConstructor, domainProcessor)
                );
            } catch (Exception e) {
                throw exceptionHandler.handle(query, criteria, e);
            }
        } else if (engine instanceof MapInvocationEngine) {
            Map<String, Object> map = new LinkedHashMap<>();
            prepareMapForQuery(map, query, criteria);
            prepareMapForPage(map, criteria, pageStartsWith0);
            try {
                result = engine.invoke(selection.getInvocation(), map);
            } catch (Exception e) {
                throw exceptionHandler.handle(query, criteria, e);
            }
        } else
            throw new UnsupportedOperationException("Engine invocation must be inherited from ArgumentsInvocationEngine or MapInvocationEngine");

        return result;
    }

    private CollectionPage<DataSet> executePageQuery(N2oQuery.Selection selection, CompiledQuery query, N2oPreparedCriteria criteria) {
        if (criteria != null && criteria.getRestrictions() != null) {
            Set<String> restrictionFieldIds = criteria.getRestrictions().stream().map(r -> r.getFieldId()).collect(Collectors.toSet());
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
                    Object value = prepareValue(filter.getCompiledDefaultValue(), filter);
                    if (value != null) {
                        Restriction defaultRestriction = new Restriction(entry.getKey(), value, filter.getType());
                        criteria.addRestriction(defaultRestriction);
                    }
                }
            }
        }
    }

    public static void prepareMapForQuery(Map<String, Object> map, CompiledQuery query, N2oPreparedCriteria criteria) {
        map.put("select", query.getSelectExpressions());
        Set<String> joins = new LinkedHashSet<>(query.getJoinExpressions());

        List<String> where = new ArrayList<>();
        for (Restriction r : criteria.getRestrictions()) {
            N2oQuery.Filter filter = query.getFiltersMap().get(r.getFieldId()).get(r.getType());
            if (filter.getText() != null)
                where.add(filter.getText());
            inMap(map, filter.getMapping(), r.getValue());
            N2oQuery.Field field = query.getFieldsMap().get(r.getFieldId());
            if (!field.getNoJoin())
                joins.add(field.getJoinBody());
        }
        map.put("filters", where);

        List<String> sortingExp = new ArrayList<>();
        if (criteria.getSorting() != null)
            for (Sorting sorting : criteria.getSortings()) {
                N2oQuery.Field field = query.getFieldsMap().get(sorting.getField());
                if (field.getNoSorting())
                    continue;
                sortingExp.add(field.getSortingBody());
                inMap(map, field.getSortingMapping(), sorting.getDirection().getExpression());
                if (!field.getNoJoin())
                    joins.add(field.getJoinBody());
            }
        map.put("sorting", sortingExp);

        if (criteria.getAdditionalFields() != null) {
            criteria.getAdditionalFields().entrySet().stream().filter(es -> es.getValue() != null)
                    .forEach(es -> map.put(es.getKey(), es.getValue()));
        }

        map.put("join", new ArrayList<>(joins));
    }

    public static void prepareMapForPage(Map<String, Object> map, N2oPreparedCriteria criteria, boolean pageStartsWith0) {
        map.put("limit", criteria.getSize());
        map.put("offset", criteria.getFirst());
        if (criteria.getCount() != null)
            map.put("count", criteria.getCount());
        map.put("page", pageStartsWith0 ? criteria.getPage() - 1 : criteria.getPage());
    }

    private Object prepareValue(Object value, N2oQuery.Filter filter) {
        Object result = value;
        result = contextProcessor.resolve(result);
        result = domainProcessor.deserialize(result, filter == null ? null : filter.getDomain());
        result = normalizeValue(result, filter == null ? null : filter.getNormalize(), null, parser, applicationContext);
        return result;
    }

    private DataSet prepareSingleResult(Object res, CompiledQuery query,
                                        N2oQuery.Selection selection) {
        Object result = outMap(res, selection.getResultMapping(), Object.class);
        return mapFields(result, query.getDisplayFields());
    }

    private CollectionPage<DataSet> preparePageResult(Object res, CompiledQuery query, N2oQuery.Selection
            selection,
                                                      N2oPreparedCriteria criteria) {
        Collection<?> result = outMap(res, selection.getResultMapping(), Collection.class);

        List<DataSet> content = result.stream()
                .map(obj -> mapFields(obj, query.getDisplayFields()))
                .collect(Collectors.toList());
        return getPage(content, criteria, () -> {
            if (criteria.getSize() == 1) {
                return 1;
            } else if (selection.getCountMapping() == null) {
                return executeCount(query, criteria);
            } else {
                return outMap(res, selection.getCountMapping(), Integer.class);
            }
        });
    }

    private N2oQuery.Selection chooseSelection(N2oQuery.Selection[] selections, Set<String> filterFields, String
            queryId) {
        if (selections == null) {
            return null;
        }
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
        for (N2oQuery.Selection selection : lists) {
            if (selection.getFilters() == null || selection.getFilters().isEmpty()) {
                return selection;
            }
        }
        return null;
    }

    private N2oQuery.Selection findSelectionByFilters(N2oQuery.Selection[] selections, Set<String> filterFields) {
        for (N2oQuery.Selection selection : selections) {
            if (selection.getFilters() == null || selection.getFilters().isEmpty()) {
                continue;
            }
            Set<String> filters = new HashSet<>();
            Collections.addAll(filters, selection.getFilters().split("\\s*,\\s*"));
            if (filterFields.size() == filters.size()) {
                filterFields.forEach(filters::remove);
                if (filters.isEmpty()) {
                    return selection;
                }
            }
        }
        return findBaseSelection(selections);
    }

    /**
     * Приведение значений фильтраций к домену и контексту
     */
    private void resolveRestriction(CompiledQuery query, N2oPreparedCriteria criteria) {
        Set<String> restrictionsForRemove = null;
        for (Restriction restriction : criteria.getRestrictions()) {
            N2oQuery.Filter filter = query.getFiltersMap().get(restriction.getFieldId()).get(restriction.getType());
            Object value = prepareValue(restriction.getValue(), filter);
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
        if (!query.getFieldsMap().containsKey(N2oQuery.Field.PK))
            return;
        if (!query.getFieldsMap().get(N2oQuery.Field.PK).getNoDisplay())
            return;
        int i = 1;
        for (DataSet dataSet : collectionPage.getCollection()) {
            dataSet.put(N2oQuery.Field.PK, i++);
        }
    }

    private DataSet mapFields(Object entry, List<N2oQuery.Field> fields) {
        DataSet resultDataSet = new DataSet();
        fields.forEach(f -> outMap(resultDataSet, entry, f.getId(), f.getSelectMapping(), f.getSelectDefaultValue(), contextProcessor));
        return normalizeDataSet(resultDataSet, fields);
    }

    private DataSet normalizeDataSet(DataSet dataSet, List<N2oQuery.Field> fields) {
        for (N2oQuery.Field f : fields) {
            if (f.getNormalize() != null) {
                Object obj = dataSet.get(f.getId());
                obj = contextProcessor.resolve(obj);
                dataSet.put(f.getId(), normalizeValue(obj, f.getNormalize(), dataSet, parser, applicationContext));
            }
        }
        return dataSet;
    }

    private CollectionPage<DataSet> getPage(Collection<DataSet> content, N2oPreparedCriteria criteria,
                                            Supplier<Integer> totalSupplier) {
        if (criteria.getFirst() == 0) {
            if (criteria.getSize() > content.size()) {
                return new CollectionPage<>(content.size(), content, criteria);
            }
            return new CollectionPage<>(totalSupplier.get(), content, criteria);
        }
        if (content.size() != 0 && criteria.getSize() > content.size()) {
            return new CollectionPage<>(criteria.getFirst() + content.size(), content, criteria);
        }
        return new CollectionPage<>(totalSupplier.get(), content, criteria);
    }

    public void setPageStartsWith0(boolean pageStartsWith0) {
        this.pageStartsWith0 = pageStartsWith0;
    }

    public void setCriteriaResolver(CriteriaConstructor criteriaResolver) {
        this.criteriaConstructor = criteriaResolver;
    }

    @Override
    public void setEnvironment(MetadataEnvironment environment) {
        this.contextProcessor = environment.getContextProcessor();
        this.domainProcessor = environment.getDomainProcessor();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
