package net.n2oapp.framework.engine.util;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.dataset.FieldMapping;
import net.n2oapp.framework.api.criteria.N2oPreparedCriteria;
import net.n2oapp.framework.api.criteria.Restriction;
import net.n2oapp.framework.api.data.CriteriaConstructor;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.Argument;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oArgumentsInvocation;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.n2oapp.framework.engine.util.MappingProcessor.inMap;

/**
 * Утилитный класс, служащий для преобразования данных вызова в массив аргументов
 */
public class ArgumentsInvocationUtil {
    private final static ExpressionParser writeParser = new SpelExpressionParser(new SpelParserConfiguration(true, true));
    private final static Set<String> primitiveTypes = Stream.of("java.lang.Boolean", "java.lang.Character", "java.lang.Byte",
            "java.lang.Short", "java.lang.Integer", "java.lang.Long", "java.lang.Float", "java.lang.Double", "java.util.Date",
            "java.math.BigDecimal").collect(Collectors.toSet());

    /**
     * Собирает аргументы для query, на выход будет массив из одного аргумента типа net.n2oapp.criteria.api.Criteria
     */
    public static Object[] mapToArgs(N2oArgumentsInvocation invocation, CompiledQuery query,
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
     * Собирает аргументы для действия invocation
     *
     * @param invocation Вызов действия
     * @param inDataSet  Входные данные
     * @param inMapping  Маппинг входных данных
     * @return Массив объектов
     */
    public static Object[] mapToArgs(N2oArgumentsInvocation invocation, DataSet inDataSet,
                                     Map<String, FieldMapping> inMapping, DomainProcessor domainProcessor) {
        inMapping = changeInMappingForEntity(invocation, inMapping);
        if (invocation.getArguments() == null || invocation.getArguments().length == 0)
            return null;
        return map(inDataSet, inMapping, invocation.getArguments(), domainProcessor);
    }

    /**
     * Генерирует список аргументов для вызова метода.
     *
     * @param dataSet    Исходные данные
     * @param mappingMap Правила маппинга
     * @param arguments  Список аргументов
     * @return Массив объектов
     */
    public static Object[] map(DataSet dataSet, Map<String, FieldMapping> mappingMap, Argument[] arguments,
                               DomainProcessor domainProcessor) {
        Object[] result = instantiateArguments(arguments);

        boolean hasOnlyOneEntity = result.length == 1 && result[0] != null;
        int idx = 0;

        for (Map.Entry<String, FieldMapping> map : mappingMap.entrySet()) {
            Object value = dataSet.get(map.getKey());
            String mapping = map.getValue().getMapping();
            if ((mapping != null && !mapping.startsWith("[") && !mapping.endsWith("]")) || value != null) {
                String resultMapping = mapping;
                if (resultMapping == null)
                    resultMapping = hasOnlyOneEntity ? "[0]." + map.getKey() : "[" + idx + "]";

                Expression expression = writeParser.parseExpression(resultMapping);
                expression.setValue(result, value);
            }
            idx++;
        }
        for (int i = 0; i < result.length; i++) {
            if (result[i] == null && arguments[i].getDefaultValue() != null) {
                result[i] = domainProcessor.deserialize(arguments[i].getDefaultValue());
            }
        }
        return result;
    }

    private static Object[] instantiateArguments(Argument[] arguments) {
        if (arguments == null) return null;
        Object[] argumentInstances = new Object[arguments.length];

        for (int k = 0; k < argumentInstances.length; k++) {
            String className = arguments[k].getClassName();
            Class argumentClass;
            if (className == null || primitiveTypes.contains(className)) {
                argumentInstances[k] = null;
            } else {
                try {
                    argumentClass = Class.forName(className);
                    argumentInstances[k] = argumentClass.newInstance();
                } catch (Exception e) {
                    throw new N2oException("Can't create instance of class " + className, e);
                }
            }
        }
        return argumentInstances;
    }

    /**
     * Возвращает классы аргументов вызываемого метода, используя кэширование
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

    private static Map<String, FieldMapping> changeInMappingForEntity(N2oArgumentsInvocation invocation,
                                                                      Map<String, FieldMapping> inMapping) {
        if (ArrayUtils.isEmpty(invocation.getArguments())) {
            final int[] idx = {0};
            for (Map.Entry<String, FieldMapping> entry : inMapping.entrySet()) {
                if (entry.getValue().getMapping() == null)
                    entry.getValue().setMapping(String.format("[%s]", idx[0]++));
            }
        } else {
            int entityPosition = findEntityPosition(invocation.getArguments());
            if (entityPosition != -1) {
                // позиция entity используется для создания префикса
                String prefix = "[" + entityPosition + "].";
                for (String key : inMapping.keySet()) {
                    if (inMapping.get(key) != null) {
                        String mapping = inMapping.get(key).getMapping();
                        if (mapping != null && !mapping.startsWith("["))
                            inMapping.get(key).setMapping(prefix + mapping);
                    }
                }
            }
        }
        return inMapping;
    }

    /**
     * Находит номер позиции entity в аргументах провайдера
     *
     * @param arguments Аргументы провайдера
     * @return Номер позиции entity или -1, если не был найден
     */
    private static int findEntityPosition(Argument[] arguments) {
        for (int i = 0; i < arguments.length; i++) {
            if ((arguments[i].getType() != null) &&
                    (arguments[i].getType().equals(Argument.Type.ENTITY))) {
                return i;
            }
        }
        return -1;
    }
}
