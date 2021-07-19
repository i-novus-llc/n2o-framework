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

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Утилитный класс, служащий для преобразования данных вызова в массив аргументов
 */
public class ArgumentsInvocationUtil {

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

        for (int i = 0; i < invocation.getArguments().length; i++)
            if (Argument.Type.CRITERIA.equals(invocation.getArguments()[i].getType()))
                argumentInstances[i] = criteriaConstructor.construct(criteria, argumentInstances[i]);

        if (Arrays.stream(invocation.getArguments()).filter(arg -> (Argument.Type.ENTITY.equals(arg.getType()) ||
                Argument.Type.CRITERIA.equals(arg.getType()))).collect(Collectors.toList()).size() > 1)
            throw new IllegalArgumentException("There must be only one argument with Criteria or Entity type ");

        int idx = 0;
        for (Restriction r : criteria.getRestrictions()) {
            if (r.getValue() != null) {
                N2oQuery.Filter filter = query.getFiltersMap().get(r.getFieldId()).get(r.getType());
                String mapping = getMapping(invocation.getArguments(), idx, filter.getMapping(), filter.getFilterField());
                MappingProcessor.inMap(argumentInstances, mapping, r.getValue());
            }
            idx++;
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
        if (invocation.getArguments() == null || invocation.getArguments().length == 0)
            return null;

        Object[] result = instantiateArguments(invocation.getArguments());

        int idx = 0;
        for (Map.Entry<String, FieldMapping> entry : inMapping.entrySet()) {
            if (dataSet.get(entry.getKey()) != null) {
                String mapping = getMapping(invocation.getArguments(), idx, entry.getValue().getMapping(), entry.getKey());
                MappingProcessor.inMap(result, mapping, dataSet.get(entry.getKey()));
            }
            idx++;
        }

        resolveDefaultValues(invocation.getArguments(), domainProcessor, result);
        return result;
    }

    /**
     * Получение маппинга аргумента
     *
     * @param arguments      Массив аргументов
     * @param idx            Индекс возможной позиции
     * @param mapping        Указанный маппинг
     * @param defaultMapping Маппинг по умолчанию
     * @return Маппинг аргумента
     */
    private static String getMapping(Argument[] arguments, int idx, String mapping, String defaultMapping) {
        String resultMapping;
        int argIdx;

        if (mapping != null) {
            argIdx = findArgumentPosition(arguments, mapping.substring(1, mapping.indexOf("]")).replace("'", ""));
            resultMapping = argIdx == -1 ? mapping :
                    "[" + argIdx + "]" + mapping.substring(mapping.indexOf("]") + 1);
        } else {
            argIdx = findArgumentPosition(arguments, defaultMapping);
            resultMapping = argIdx == -1 ? "[" + idx + "]" :
                    "[" + argIdx + "]";
        }

        return resultMapping;
    }


    /**
     * Находит по имени номер позиции аргумента среди всех аргументов провайдера
     *
     * @param arguments Аргументы провайдера
     * @param name      Имя аргумента
     * @return Номер позиции аргумента или -1, если не был найден
     */
    private static int findArgumentPosition(Argument[] arguments, String name) {
        for (int i = 0; i < arguments.length; i++)
            if (name.equals(arguments[i].getName()))
                return i;
        return -1;
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
}
