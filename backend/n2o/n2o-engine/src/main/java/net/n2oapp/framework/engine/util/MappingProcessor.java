package net.n2oapp.framework.engine.util;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.dataset.FieldMapping;
import net.n2oapp.framework.api.context.ContextProcessor;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectListField;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectReferenceField;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSimpleField;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.BeanFactoryResolver;
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
}
