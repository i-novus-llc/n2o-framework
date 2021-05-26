package net.n2oapp.framework.engine.util;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.dataset.FieldMapping;
import net.n2oapp.framework.api.context.ContextProcessor;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.Argument;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectListField;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectReferenceField;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.*;

/**
 * Утилитный класс для маппинга данных.
 * Пока сделан
 */
public class MappingProcessor {

    private final static ExpressionParser writeParser = new SpelExpressionParser(new SpelParserConfiguration(true, true));
    private static final ExpressionParser readParser = new SpelExpressionParser(new SpelParserConfiguration(false, false));

    /**
     * Входящее преобразование value согласно выражению mapping в объект target
     *
     * @param target  результирующий объект
     * @param mapping выражение преобразования
     * @param value   значение
     */
    public static void inMap(Object target, String mapping, Object value) {
        Expression expression = writeParser.parseExpression(mapping);
        if (target != null)
            expression.setValue(target, value);
    }

    /**
     * Исходящее преобразование target согласно mapping выражению
     *
     * @param target  исходное значение
     * @param mapping выражения преобразования
     * @return результат преобразования
     */
    public static <T> T outMap(Object target, String mapping, Class<T> clazz) {
        T result;
        if (mapping != null) {
            Expression expression = readParser.parseExpression(mapping);
            result = expression.getValue(target, clazz);
        } else {
            result = (T) target;
        }
        if (clazz != null && result == null)
            throw new N2oException("Expected is " + clazz + ", but actual is null");
        if (clazz != null && !clazz.isAssignableFrom(result.getClass()))
            throw new N2oException("Expected is " + clazz + ", but actual is " + result.getClass());
        return result;
    }

    /**
     * Исходящее преобразование value согласно mapping и в target под ключом fieldId
     * В случае если результат после маппинга = null, в target добавляется  defaultValue
     *
     * @param target       данные результирующие
     * @param value        исходное значение
     * @param fieldId      идентификатор поля
     * @param mapping      выражение преобразования
     * @param defaultValue значение по умолчанию
     */
    public static void outMap(DataSet target, Object value, String fieldId, String mapping, Object defaultValue, ContextProcessor contextProcessor) {
        Expression expression = readParser.parseExpression(mapping);
        Object obj = expression.getValue(value);
        target.put(fieldId, obj == null ? contextProcessor.resolve(defaultValue) : obj);
    }

    /**
     * Генерирует список аргументов для вызова метода.
     *
     * @param dataSet    Исходные данные
     * @param mappingMap Правила маппинга
     * @param arguments  Список аргументов
     * @return Массив объектов
     */
    public static Map<String, Object> map(DataSet dataSet, Map<String, FieldMapping> mappingMap,
                                          Argument[] arguments) {
        Map<String, Object> result = new HashMap<>();
        boolean hasOnlyOneEntity = arguments.length == 1 && Argument.Type.ENTITY.equals(arguments[0].getType());
        int idx = 0;

        for (Map.Entry<String, FieldMapping> map : mappingMap.entrySet()) {
            Object value = dataSet.get(map.getKey());
            String mapping = map.getValue().getMapping();
            if ((mapping != null && !mapping.startsWith("[") && !mapping.endsWith("]")) || value != null) {
                String resultMapping = mapping;
                if (resultMapping == null)
                    resultMapping = hasOnlyOneEntity ? "[0]." + map.getKey() : "[" + idx + "]";

                result.put(resultMapping, value);
            }
            idx++;
        }
        return result;
    }

    /**
     * Заменяет в inDataSet значение созданным объектом
     *
     * @param parameter параметр операции
     * @param dataSet   исходные данные
     */
    public static void mapParameter(ObjectReferenceField parameter, DataSet dataSet) {
        Object data = dataSet.get(parameter.getId());
        if (data == null)
            return;
        if (parameter.getClass().equals(ObjectReferenceField.class)) {
            dataSet.put(parameter.getId(), mapChildParameters(parameter, (DataSet) data));
        } else {
            Collection collection = parameter instanceof ObjectListField ? new ArrayList() : new HashSet();
            for (Object item : (Collection) data)
                collection.add(mapChildParameters(parameter, (DataSet) item));
            dataSet.put(parameter.getId(), collection);
        }
    }

    /**
     * Создает инстанс и мапит его поля из dataSet
     *
     * @param parameter Параметр операции
     * @param dataSet   Исходные данные
     */
    public static Object mapChildParameters(ObjectReferenceField parameter, DataSet dataSet) {
        Object instance;
        try {
            instance = Class.forName(parameter.getEntityClass()).newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new N2oException(e);
        }

        for (AbstractParameter childParam : (parameter).getFields()) {
            String target = childParam.getMapping() != null ? childParam.getMapping() : childParam.getId();
            writeParser.parseExpression(target).setValue(instance, dataSet.get(childParam.getId()));
        }
        return instance;
    }
}
