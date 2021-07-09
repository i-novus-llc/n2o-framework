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

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static net.n2oapp.framework.engine.util.MappingProcessor.inMap;

/**
 * Утилитный класс, служащий для преобразования данных вызова в массив аргументов
 */
public class ArgumentsInvocationUtil {
    private final static ExpressionParser writeParser = new SpelExpressionParser(new SpelParserConfiguration(true, true));


    /**
     * Собирает аргументы для действия invocation в выборке
     *
     * @param invocation          Вызов действия
     * @param query               Скомпилированная модель запроса за данными
     * @param criteria            Критерий фильтрации данных
     * @param criteriaConstructor Конструктор критериев
     * @param domainProcessor     Процессор приведения к типу
     * @return Массив объектов
     */
    public static Object[] mapToArgs(N2oArgumentsInvocation invocation, CompiledQuery query,
                                     N2oPreparedCriteria criteria, CriteriaConstructor criteriaConstructor,
                                     DomainProcessor domainProcessor) {
        Object[] argumentInstances = instantiateArguments(invocation.getArguments());
        if (ArrayUtils.isEmpty(argumentInstances))
            return new Object[0];

        int criteriaIdx = -1;
        for (int i = 0; i < invocation.getArguments().length; i++) {
            if (Argument.Type.CRITERIA.equals(invocation.getArguments()[i].getType())) {
                argumentInstances[i] = criteriaConstructor.construct(criteria, argumentInstances[i]);
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

        resolveDefaultValues(invocation.getArguments(), domainProcessor, argumentInstances);
        return argumentInstances;
    }

    /**
     * Собирает аргументы для действия invocation
     *
     * @param invocation      Вызов действия
     * @param dataSet         Входные данные
     * @param inMapping       Маппинг входных данных
     * @param domainProcessor Процессор приведения к типу
     * @return Массив объектов
     */
    public static Object[] mapToArgs(N2oArgumentsInvocation invocation, DataSet dataSet,
                                     Map<String, FieldMapping> inMapping, DomainProcessor domainProcessor) {
        inMapping = changeInMappingForEntity(invocation, inMapping);
        if (invocation.getArguments() == null || invocation.getArguments().length == 0)
            return null;

        Object[] result = instantiateArguments(invocation.getArguments());
        boolean hasOnlyOneEntity = result.length == 1 && result[0] != null;
        int idx = 0;

        for (Map.Entry<String, FieldMapping> map : inMapping.entrySet()) {
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

        resolveDefaultValues(invocation.getArguments(), domainProcessor, result);
        return result;
    }

    /**
     * Задание значений по умолчанию данным вызова, если их значение не задано
     *
     * @param arguments       Аргументы вызова
     * @param domainProcessor Процессор приведения к типу
     * @param results         Данные вызова
     */
    private static void resolveDefaultValues(Argument[] arguments, DomainProcessor domainProcessor, Object[] results) {
        for (int i = 0; i < results.length; i++) {
            String defaultValue = arguments[i].getDefaultValue();
            if (results[i] == null && defaultValue != null)
                results[i] = domainProcessor.deserialize(defaultValue);
        }
    }

    /**
     * Создание массива экземпляров по классам аргументов
     *
     * @param arguments Массив аргументов
     * @return Массив экземпляров
     */
    private static Object[] instantiateArguments(Argument[] arguments) {
        if (arguments == null) return null;
        Object[] argumentInstances = new Object[arguments.length];

        for (int k = 0; k < argumentInstances.length; k++) {
            String className = arguments[k].getClassName();
            Class argumentClass;
            if (className == null || arguments[k].getType() == null ||
                    Argument.Type.PRIMITIVE.equals(arguments[k].getType())) {
                argumentInstances[k] = null;
            } else {
                try {
                    argumentClass = ClassHash.getClass(className);
                    argumentInstances[k] = argumentClass.newInstance();
                } catch (Exception e) {
                    throw new N2oException("Can't create instance of class " + className, e);
                }
            }
        }
        return argumentInstances;
    }

    /**
     * Изменение маппинга вызова согласно наличию entity аргумента
     *
     * @param invocation Вызов действия
     * @param inMapping  Маппинг входных данных
     * @return Измененный маппинг вызова
     */
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
