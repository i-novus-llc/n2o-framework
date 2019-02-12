package net.n2oapp.framework.engine.data;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.context.ContextProcessor;
import net.n2oapp.framework.api.criteria.N2oPreparedCriteria;
import net.n2oapp.framework.api.criteria.Restriction;
import net.n2oapp.framework.api.data.*;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oArgumentsInvocation;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.engine.exception.N2oFoundMoreThanOneRecordException;
import net.n2oapp.framework.engine.exception.N2oRecordNotFoundException;
import net.n2oapp.framework.engine.util.InvocationParametersMapping;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static net.n2oapp.framework.engine.util.InvocationParametersMapping.normalizeValue;
import static net.n2oapp.framework.engine.util.MappingProcessor.outMap;

/**
 * Процессор выборок
 */
public class N2oQueryProcessor implements QueryProcessor {
    private static final ExpressionParser parser = new SpelExpressionParser();

    private ContextProcessor contextProcessor;
    private N2oInvocationFactory invocationFactory;
    private CriteriaConstructor criteriaConstructor = new N2oCriteriaConstructor(false);
    private DomainProcessor domainProcessor;
    private QueryExceptionHandler exceptionHandler;
    private boolean pageStartsWith0;

    public N2oQueryProcessor(N2oInvocationFactory invocationFactory,
                             ContextProcessor contextProcessor,
                             DomainProcessor domainProcessor,
                             QueryExceptionHandler exceptionHandler) {
        this.domainProcessor = domainProcessor;
        this.contextProcessor = contextProcessor;
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
                result = ((ArgumentsInvocationEngine) engine).invoke((N2oArgumentsInvocation) selection.getInvocation(),
                        InvocationParametersMapping.prepareArgsForQuery((N2oArgumentsInvocation) selection.getInvocation(), query, criteria, criteriaConstructor));
            } catch (Exception e) {
                throw exceptionHandler.handle(query, criteria, e);
            }
        } else {
            Map<String, Object> map = new LinkedHashMap<>();
            InvocationParametersMapping.prepareMapForQuery(map, query, criteria);
            try {
                result = engine.invoke(selection.getInvocation(), map);
            } catch (Exception e) {
                throw exceptionHandler.handle(query, criteria, e);
            }
        }
        return outMap(result, selection.getCountMapping(), Integer.class);
    }


    public CollectionPage<DataSet> executeOneSizeQuery(CompiledQuery query, N2oPreparedCriteria criteria) {
        criteria.setCount(2);
        N2oQuery.Selection selection = findUniqueSelection(query, criteria);
        Object result = executeQuery(selection, query, criteria);
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
            if (single == null) {
                throw new N2oRecordNotFoundException();
            }
            return new CollectionPage<>(1, Collections.singletonList(single), criteria);
        } else
            throw new UnsupportedOperationException();
    }

    private N2oQuery.Selection findCountSelection(final CompiledQuery query, final N2oPreparedCriteria criteria) {
        List<String> filterFields = criteria.getRestrictions() == null ? new ArrayList<>() :
                criteria.getRestrictions().stream().map(Restriction::getFieldId).collect(Collectors.toList());
        N2oQuery.Selection selection = chooseSelection(query.getCounts(), filterFields, query.getId());
        if (selection == null)
            throw new N2oException("Can't find selection for count request");
        return selection;
    }


    private N2oQuery.Selection findUniqueSelection(final CompiledQuery query, final N2oPreparedCriteria criteria) {
        resolveRestriction(query, criteria);
        addDefaultFilters(query, criteria);
        List<String> filterFields = criteria.getRestrictions() == null ? new ArrayList<>() :
                criteria.getRestrictions().stream().map(Restriction::getFieldId).collect(Collectors.toList());
        N2oQuery.Selection selection = chooseSelection(query.getUniques(), filterFields, query.getId());
        if (selection != null)
            return selection;
        selection = chooseSelection(query.getLists(), filterFields, query.getId());
        if (selection == null)
            throw new N2oException("Can't find selection for unique request");
        return selection;
    }

    private N2oQuery.Selection findListSelection(final CompiledQuery query, final N2oPreparedCriteria criteria) {
        resolveRestriction(query, criteria);
        addDefaultFilters(query, criteria);
        List<String> filterFields = criteria.getRestrictions() == null ? new ArrayList<>() :
                criteria.getRestrictions().stream().map(Restriction::getFieldId).collect(Collectors.toList());
        N2oQuery.Selection selection = chooseSelection(query.getLists(), filterFields, query.getId());
        if (selection == null)
            throw new N2oException("Can't find selection for list request");
        return selection;
    }

    @SuppressWarnings("unchecked")
    private Object executeQuery(N2oQuery.Selection selection, CompiledQuery query, N2oPreparedCriteria criteria) {
        ActionInvocationEngine engine = invocationFactory.produce(selection.getInvocation().getClass());
        Object result;
        if (engine instanceof ArgumentsInvocationEngine) {
            try {
                result = ((ArgumentsInvocationEngine) engine).invoke((N2oArgumentsInvocation) selection.getInvocation(),
                        InvocationParametersMapping.prepareArgsForQuery((N2oArgumentsInvocation) selection.getInvocation(), query, criteria, criteriaConstructor));
            } catch (UnsupportedOperationException e) {
                throw exceptionHandler.handle(query, criteria, e);
            }
        } else if (engine instanceof MapInvocationEngine) {
            Map<String, Object> map = new LinkedHashMap<>();
            InvocationParametersMapping.prepareMapForQuery(map, query, criteria);
            InvocationParametersMapping.prepareMapForPage(map, criteria, pageStartsWith0);
            try {
                result = engine.invoke(selection.getInvocation(), map);
            } catch (UnsupportedOperationException e) {
                throw exceptionHandler.handle(query, criteria, e);
            }
        } else
            throw new UnsupportedOperationException("Engine invocation must be inherited from ArgumentsInvocationEngine or MapInvocationEngine");

        return result;
    }

    private CollectionPage<DataSet> executePageQuery(N2oQuery.Selection selection, CompiledQuery query, N2oPreparedCriteria criteria) {
        Object result = executeQuery(selection, query, criteria);
        CollectionPage<DataSet> page = preparePageResult(result, query, selection, criteria);
        addIdIfNotPresent(query, page);
        return page;
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

    private Object prepareValue(Object value, N2oQuery.Filter filter) {
        Object result = value;
        result = contextProcessor.resolve(result);
        result = domainProcessor.deserialize(result, filter.getDomain());
        result = normalizeValue(result, filter.getNormalize(), null, parser);
        return result;
    }

    private DataSet prepareSingleResult(Object res, CompiledQuery query,
                                        N2oQuery.Selection selection) {
        Object result = outMap(res, selection.getResultMapping(), Object.class);
        return mapFields(result, query.getDisplayFields());
    }

    private CollectionPage<DataSet> preparePageResult(Object res, CompiledQuery query, N2oQuery.Selection selection,
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

    private N2oQuery.Selection chooseSelection(N2oQuery.Selection[] selections, List<String> filterFields, String queryId) {
        if (selections == null) {
            return null;
        }
        if (filterFields == null) {
            N2oQuery.Selection result = findBaseSelection(selections);
            if (result == null) {
                throw new N2oException("Can't find query without filters in query " + queryId);
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

    private N2oQuery.Selection findSelectionByFilters(N2oQuery.Selection[] selections, List<String> filterFields) {
        for (N2oQuery.Selection selection : selections) {
            if (selection.getFilters() == null || selection.getFilters().isEmpty()) {
                continue;
            }
            Set<String> filters = new HashSet<>();
            Collections.addAll(filters, selection.getFilters().split(","));
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
        fields.forEach(f -> outMap(resultDataSet, entry, f.getId(), f.getSelectMapping(), f.getSelectDefaultValue()));
        return normalizeDataSet(resultDataSet, fields);
    }

    private DataSet normalizeDataSet(DataSet dataSet, List<N2oQuery.Field> fields) {
        for (N2oQuery.Field f : fields) {
            if (f.getNormalize() != null) {
                Object obj = dataSet.get(f.getId());
                obj = contextProcessor.resolve(obj);
                dataSet.put(f.getId(), normalizeValue(obj, f.getNormalize(), dataSet, parser));
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
}
