package net.n2oapp.framework.engine.util;

import net.n2oapp.criteria.api.Sorting;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.dataset.DataSetMapper;
import net.n2oapp.criteria.dataset.FieldMapping;
import net.n2oapp.framework.api.criteria.N2oPreparedCriteria;
import net.n2oapp.framework.api.criteria.Restriction;
import net.n2oapp.framework.api.data.CriteriaConstructor;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.Argument;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oArgumentsInvocation;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectReferenceField;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSimpleField;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.*;
import java.util.stream.Collectors;

import static net.n2oapp.framework.engine.util.MappingProcessor.inMap;

/**
 * Утилитный класс для преобразования аргументов при вызове действий
 */
public class InvocationParametersMapping {

    /**
     * Получение маппингов исходящих полей
     *
     * @param parameters Список исходящих полей
     * @return Маппинги исходящих полей
     */
    public static Map<String, String> extractOutFieldMapping(Collection<ObjectSimpleField> parameters) {
        Map<String, String> mappingMap = new LinkedHashMap<>();
        if (parameters != null)
            for (ObjectSimpleField parameter : parameters)
                mappingMap.put(parameter.getId(), parameter.getMapping());
        return mappingMap;
    }

    /**
     * Получение структуры маппингов входящих полей
     *
     * @param parameters Список входящих полей
     * @return Структура маппингов исходящих полей
     */
    public static Map<String, FieldMapping> extractInFieldMapping(Collection<AbstractParameter> parameters) {
        Map<String, FieldMapping> mappingMap = new LinkedHashMap<>();
        if (parameters != null)
            for (AbstractParameter parameter : parameters) {
                FieldMapping mapping = new FieldMapping(parameter.getMapping());
                if (parameter instanceof ObjectReferenceField && ((ObjectReferenceField) parameter).getFields() != null)
                    mapping.setChildMapping(extractInFieldMapping(Arrays.asList(((ObjectReferenceField) parameter).getFields())));
                mappingMap.put(parameter.getId(), mapping);
            }
        return mappingMap;
    }

    /**
     * Собирает аргументы для действия invocation
     *
     * @param invocation Вызов действия
     * @param inDataSet  Входные данные
     * @param inMapping  Маппинг входных данных
     * @return
     */
    public static Object[] mapToArgs(N2oArgumentsInvocation invocation, DataSet inDataSet,
                                     Map<String, FieldMapping> inMapping, DomainProcessor domainProcessor) {
        inMapping = changeInMappingForEntity(invocation, inMapping);
        if (invocation.getArguments() == null || invocation.getArguments().length == 0)
            return null;
        return MappingProcessor.map(inDataSet, inMapping, invocation.getArguments(), domainProcessor);
    }

    /**
     * Преобразует входные значения согласно маппингу и собирает их в map
     *
     * @param dataSet Входные данные
     * @param mapping Маппинг полей
     * @return Преобразованные входные значения согласно маппингу
     */
    public static Map<String, Object> mapToMap(DataSet dataSet, Map<String, FieldMapping> mapping) {
        return DataSetMapper.mapToMap(dataSet, mapping);
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


    /**
     * Собирает аргументы для query, на выход будет массив из одного аргумента типа net.n2oapp.criteria.api.Criteria
     *
     * @param invocation          вызов действия с одним аргументом типа net.n2oapp.criteria.api.Criteria
     * @param query
     * @param criteria
     * @param criteriaConstructor
     * @param domainProcessor
     * @return
     */
    public static Object[] prepareArgsForQuery(N2oArgumentsInvocation invocation, CompiledQuery query,
                                               N2oPreparedCriteria criteria, CriteriaConstructor criteriaConstructor,
                                               DomainProcessor domainProcessor) {

        Class<?>[] classesOfArguments = takeClassesOfArguments(invocation);
        if (classesOfArguments == null || classesOfArguments.length == 0)
            return new Object[0];
        Object[] argumentInstances = new Object[classesOfArguments.length];
        int criteriaIdx = -1;
        for (int i = 0; i < invocation.getArguments().length; i++) {
            if (classesOfArguments[i] == null)
                continue;
            if (invocation.getArguments()[i].getType().equals(Argument.Type.CLASS) || invocation.getArguments()[i].getType().equals(Argument.Type.ENTITY)) {
                try {
                    argumentInstances[i] = classesOfArguments[i].newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new IllegalArgumentException(classesOfArguments[i].getName(), e);
                }
            }

            if (invocation.getArguments()[i].getType().equals(Argument.Type.CRITERIA)) {
                argumentInstances[i] = criteriaConstructor.construct(criteria, classesOfArguments[i]);
                criteriaIdx = i;
            }
        }
        if (Arrays.stream(invocation.getArguments()).filter(arg -> (arg.getType().equals(Argument.Type.ENTITY) ||
                arg.getType().equals(Argument.Type.CRITERIA))).collect(Collectors.toList()).size() > 1)
            throw new IllegalArgumentException("There must be only one argument with Criteria or Entity type ");
        for (Restriction r : criteria.getRestrictions()) {
            N2oQuery.Filter filter = query.getFiltersMap().get(r.getFieldId()).get(r.getType());
            String mapping = filter.getMapping().startsWith("[") ? filter.getMapping() : "[" + criteriaIdx + "]." + filter.getMapping();
            inMap(argumentInstances, mapping, r.getValue());
        }
        for (int i = 0; i < argumentInstances.length; i++) {
            String defaultValue = invocation.getArguments()[i].getDefaultValue();
            if (argumentInstances[i] == null && defaultValue != null)
                argumentInstances[i] = domainProcessor.deserialize(defaultValue);
        }
        return argumentInstances;
    }

    /**
     * Меняет inMapping для type="entity",
     * "name" -> "[0].name"
     *
     * @param invocation параметры метода
     * @param inMapping
     * @return innMapping вида [0].name
     */
    private static Map<String, FieldMapping> changeInMappingForEntity(N2oArgumentsInvocation invocation,
                                                                      Map<String, FieldMapping> inMapping) {
        if (ArrayUtils.isEmpty(invocation.getArguments())) {
            final int[] idx = {0};
            Map<String, FieldMapping> newMap = new HashMap<>();
            inMapping.forEach((k, v) -> {
                String mapping = v.getMapping() != null ? v.getMapping() : String.format("[%s]", idx[0]++);
                FieldMapping fieldMapping = new FieldMapping(mapping);
                fieldMapping.setChildMapping(v.getChildMapping());
                newMap.put(k, fieldMapping);
            });
            inMapping = newMap;
        } else {
            int entityPosition = findEntityPosition(invocation);
            if (entityPosition != -1) {
                // позиция entity используется для создания префикса
                String prefix = "[" + entityPosition + "].";
                for (String key : inMapping.keySet()) {
                    if (inMapping.get(key) != null) {
                        String value = inMapping.get(key).getMapping();
                        if (value != null && !value.startsWith("["))
                            inMapping.get(key).setMapping(prefix + value);
                    }
                }
            }
        }
        return inMapping;
    }

    public static Object normalizeValue(Object value, String normalizer, DataSet allData,
                                        ExpressionParser parser,
                                        BeanFactory beanFactory) {
        if (normalizer == null)
            return value;
        StandardEvaluationContext context = new StandardEvaluationContext(value);
        if (allData != null)
            context.setVariable("data", allData);
        if (beanFactory != null)
            context.setBeanResolver(new BeanFactoryResolver(beanFactory));
        Expression exp = parser.parseExpression(normalizer);
        return exp.getValue(context);
    }

    /**
     * Находит номер позиции entity в аргументах метода
     *
     * @param invocation параметры метода
     * @return номер позиции entity или -1, если нет типа entity
     */
    private static int findEntityPosition(N2oArgumentsInvocation invocation) {
        for (int i = 0; i < invocation.getArguments().length; i++) {
            if ((invocation.getArguments()[i].getType() != null) &&
                    (invocation.getArguments()[i].getType().equals(Argument.Type.ENTITY))) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Возвращает классы аргументов вызываемого метода, используя кэширование
     *
     * @param invocation параметры метода
     * @return массив классов
     */
    private static Class<?>[] takeClassesOfArguments(N2oArgumentsInvocation invocation) {
        int argumentCount = invocation.getArguments() != null ? invocation.getArguments().length : 0;
        Class<?>[] classesOfArguments = new Class[argumentCount];
        if (invocation.getArguments() == null) return null;
        try {
            for (int i = 0; i < argumentCount; i++) {
                if (invocation.getArguments()[i].getClassName() == null
                        || invocation.getArguments()[i].getType().equals(Argument.Type.PRIMITIVE))
                    classesOfArguments[i] = null;
                else
                    classesOfArguments[i] = ClassHash.getClass(invocation.getArguments()[i].getClassName());
            }
        } catch (Exception e) {
            throw new N2oException("Class of argument not found", e);
        }
        return classesOfArguments;
    }

}
