package net.n2oapp.framework.engine.util;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.dataset.FieldMapping;
import net.n2oapp.framework.api.context.ContextProcessor;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectListField;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectReferenceField;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSimpleField;
import net.n2oapp.framework.engine.data.normalize.NormalizerCollector;
import net.n2oapp.framework.engine.exception.N2oSpelException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.*;

/**
 * Утилитный класс для маппинга данных.
 */
public class MappingProcessor {
    private final static ExpressionParser writeParser = new SpelExpressionParser(new SpelParserConfiguration(true, true));
    private static final ExpressionParser readParser = new SpelExpressionParser(new SpelParserConfiguration(false, false));
    private static final Map<String, Object> registeredFunctions = new HashMap<>(NormalizerCollector.collect());

    /**
     * Входящее преобразование value согласно выражению mapping в объект target
     *
     * @param target  результирующий объект
     * @param mapping выражение преобразования
     * @param value   значение
     */
    public static void inMap(Object target, String mapping, Object value) {
        inMap(target, mapping, value, mapping);
    }

    /**
     * Входящее преобразование value согласно выражению mapping в объект target
     *
     * @param target      результирующий объект
     * @param mapping     выражение преобразования
     * @param value       значение
     * @param userMapping выражение преобразования, используемое для формирования сообщения об ошибке
     */
    public static void inMap(Object target, String mapping, Object value, String userMapping) {
        try {
            Expression expression = writeParser.parseExpression(mapping);
            if (target != null)
                expression.setValue(target, value);
        } catch (Exception e) {
            throw new N2oSpelException(userMapping, e);
        }
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
            try {
                Expression expression = readParser.parseExpression(mapping);
                result = expression.getValue(target, clazz);
            } catch (Exception e) {
                throw new N2oSpelException(mapping, e);
            }
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
        try {
            Expression expression = readParser.parseExpression(mapping);
            Object obj = expression.getValue(value);
            target.put(fieldId, obj == null ? contextProcessor.resolve(defaultValue) : obj);
        } catch (Exception e) {
            throw new N2oSpelException(fieldId, mapping, e);
        }
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
            try {
                writeParser.parseExpression(target).setValue(instance, dataSet.get(childParam.getId()));
            } catch (Exception e) {
                throw new N2oSpelException(parameter.getId(), target, e);
            }
        }
        return instance;
    }

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
                mapping.setEnabled(parameter.getEnabled());
                if (parameter instanceof ObjectReferenceField && ((ObjectReferenceField) parameter).getFields() != null)
                    mapping.setChildMapping(extractInFieldMapping(Arrays.asList(((ObjectReferenceField) parameter).getFields())));
                mappingMap.put(parameter.getId(), mapping);
            }
        return mappingMap;
    }

    /**
     * Нормализация значения по SpEL выражению
     *
     * @param value       Значение для нормализации
     * @param normalizer  Нормализируещее выражение
     * @param allData     Данные, используемые для нормализации
     * @param parser      Парсер SpEL выражений
     * @param beanFactory Фабрика бинов спринга
     * @return Нормализированное значение
     */
    public static Object normalizeValue(Object value, String normalizer, DataSet allData,
                                        ExpressionParser parser,
                                        BeanFactory beanFactory) {
        if (normalizer == null)
            return value;
        StandardEvaluationContext context = new StandardEvaluationContext(value);
        context.setVariables(registeredFunctions);
        if (allData != null)
            context.setVariable("data", allData);
        if (beanFactory != null)
            context.setBeanResolver(new BeanFactoryResolver(beanFactory));
        try {
            Expression exp = parser.parseExpression(normalizer);
            return exp.getValue(context);
        } catch (Exception e) {
            throw new N2oSpelException(normalizer, e);
        }
    }

    /**
     * Вычисление значения условия SpEl выражения
     *
     * @param condition Условное SpEl выражение
     * @param data      Исходные данные
     * @return true/false
     */
    public static Boolean resolveCondition(String condition, Map<String, Object> data) {
        StandardEvaluationContext context = new StandardEvaluationContext(data);
        context.setVariables(registeredFunctions);
        context.addPropertyAccessor(new MapAccessor());
        try {
            Expression expression = readParser.parseExpression(condition);
            return expression.getValue(context, Boolean.class);
        } catch (Exception e) {
            throw new N2oSpelException(condition, e);
        }
    }
}
