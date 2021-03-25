package net.n2oapp.criteria.dataset;

import net.n2oapp.criteria.api.CollectionPage;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * User: iryabov
 * Date: 27.08.13
 * Time: 17:32
 */
public class DataSetMapper {
    private static final ExpressionParser writeParser = new SpelExpressionParser(new SpelParserConfiguration(true, true));
    private static final ExpressionParser readParser = new SpelExpressionParser(new SpelParserConfiguration(false, false));

    private static final Set<String> primitiveTypes = new HashSet<>(Arrays.asList("java.lang.Boolean", "java.lang.Character",
            "java.lang.Byte", "java.lang.Short", "java.lang.Integer", "java.lang.Long", "java.lang.Float", "java.lang.Double",
            "java.util.Date", "java.math.BigDecimal", "net.n2oapp.criteria.dataset.DataSet", "java.util.List"));

    public static Object[] map(DataSet dataSet, Map<String, String> mapping, String... argumentClasses) {
        Object[] instances = instantiateArguments(argumentClasses);
        Object[] result;
        if (instances == null || instances.length == 0) {
            result = new Object[mapping.size()];
        } else {
            result = instances;
        }
        int idx = 0;
        for (Map.Entry<String, String> map : mapping.entrySet()) {
            Expression expression = writeParser.parseExpression(map.getValue() != null ? map.getValue()
                    : "[" + idx + "]");
            expression.setValue(result, dataSet.get(map.getKey()));
            idx++;
        }
        return result;
    }

    public static Map<String, Object> mapToMap(DataSet dataSet, Map<String, FieldMapping> mapping) {
        validateMapping(mapping);
        Map<String, Object> result = new DataSet();

        for (Map.Entry<String, FieldMapping> map : mapping.entrySet()) {
            Expression expression;
            if (map.getValue() != null) {
                expression = writeParser.parseExpression(
                        map.getValue().getMapping() != null ? map.getValue().getMapping() : "['" + map.getKey() + "']");
                if (map.getValue().getChildMapping() != null) {
                    if (map.getKey() != null) {
                        Object data = dataSet.get(map.getKey());
                        if (data instanceof Collection) {
                            Collection collection = data instanceof List ? new ArrayList() : new HashSet();
                            for (Object obj : (Collection) data)
                                collection.add(mapToMap((DataSet) obj, map.getValue().getChildMapping()));
                            expression.setValue(result, collection);
                        } else if (dataSet.get(map.getKey()) instanceof DataSet)
                            expression.setValue(result, mapToMap((DataSet) dataSet.get(map.getKey()), map.getValue().getChildMapping()));
                    }
                } else
                    expression.setValue(result, dataSet.get(map.getKey()));
            } else {
                expression = writeParser.parseExpression("['" + map.getKey() + "']");
                expression.setValue(result, dataSet.get(map.getKey()));
            }
        }
        return result;
    }

    public static DataSet extract(Object source, Map<String, String> fieldsMapping) {
        DataSet dataSet = new DataSet();
        for (Map.Entry<String, String> map : fieldsMapping.entrySet()) {
            Expression expression = readParser.parseExpression(map.getValue() != null ? map.getValue()
                    : "['" + map.getKey() + "']");
            Object value = expression.getValue(source);
            dataSet.put(map.getKey(), value);
        }
        return dataSet;
    }

    public static CollectionPage<DataSet> extract(CollectionPage<Object> sourceCollectionPage) {
        List<DataSet> page = new ArrayList<>(sourceCollectionPage.getCollection().size());
        BeanInfo info = null;
        for (Object value : sourceCollectionPage.getCollection()) {
            if (info == null) {
                try {
                    info = Introspector.getBeanInfo(value.getClass());
                } catch (IntrospectionException e) {
                    throw new RuntimeException(e);
                }
            }
            DataSet result = new DataSet();
            try {
                for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
                    Method reader = pd.getReadMethod();
                    if (reader != null)
                        result.put(pd.getName(), reader.invoke(value));
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            page.add(result);
        }
        CollectionPage<DataSet> collectionPage = new CollectionPage<>();
        collectionPage.init(sourceCollectionPage.getCount(), page);
        return collectionPage;
    }

    private static Object[] instantiateArguments(String[] arguments) {
        if (arguments == null) return null;
        Object[] argumentInstances = new Object[arguments.length];
        for (int k = 0; k < arguments.length; k++) {
            Class argumentClass;
            if (primitiveTypes.contains(arguments[k])) {
                argumentInstances[k] = null;
            } else {
                try {
                    argumentClass = Class.forName(arguments[k]);
                    argumentInstances[k] = argumentClass.newInstance();
                } catch (Exception e) {
                    throw new InstantiateArgumentException(arguments[k], e);
                }
            }
        }
        return argumentInstances;
    }

    private static final Predicate<String> MAPPING_PATTERN = Pattern.compile("\\['.+']").asPredicate();
    private static final String KEY_ERROR = "%s -> %s";

    /**
     * Проверка корректности формата маппингов
     *
     * @param mapping Map маппингов
     */
    private static void validateMapping(Map<String, FieldMapping> mapping) {
        String errorMapping = mapping.entrySet().stream()
                .filter(e -> e.getValue() != null && e.getValue().getMapping() != null)
                .filter(e -> !MAPPING_PATTERN.test(e.getValue().getMapping()))
                .map(e -> String.format(KEY_ERROR, e.getKey(), e.getValue().getMapping()))
                .collect(Collectors.joining(", "));

        if (!errorMapping.isEmpty())
            throw new IllegalArgumentException("Not valid mapping: " + errorMapping);
    }
}
