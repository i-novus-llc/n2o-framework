package net.n2oapp.framework.engine.data.action;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.ArgumentsInvocationEngine;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.global.dao.invocation.java.JavaInvocation;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.Argument;
import net.n2oapp.framework.engine.util.ClassHash;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Абстрактный сервис вызова java методов заданных в JavaInvocation
 */
@Deprecated
public abstract class JavaInvocationEngine<T extends JavaInvocation> implements ArgumentsInvocationEngine<T>{
    private final static ExpressionParser writeParser = new SpelExpressionParser(new SpelParserConfiguration(true, true));
    private final static Set<String> primitiveTypes = Stream.of("java.lang.Boolean", "java.lang.Character", "java.lang.Byte",
            "java.lang.Short", "java.lang.Integer", "java.lang.Long", "java.lang.Float", "java.lang.Double", "java.util.Date",
            "java.math.BigDecimal").collect(Collectors.toSet());

    /**
     * Return arguments of invocation method considering inMapping
     *
     * @param method    параметры метода
     * @param dataSet
     * @param inMapping
     * @return массив аргументов
     */
    protected Object[] takeArguments(JavaInvocation method, DataSet dataSet, Map<String, String> inMapping) {
        inMapping = changeInMappingForEntity(method, inMapping);
        Class<?>[] classesOfArguments = takeClassesOfArguments(method);
        Object[] argumentInstances = new Object[classesOfArguments.length];
        try {
            for (int i = 0; i < argumentInstances.length; i++) {
                if (primitiveTypes.contains(method.getArguments()[i].getClassName())) {
                    argumentInstances[i] = null;
                } else {
                    argumentInstances[i] = classesOfArguments[i].newInstance();
                }
            }
            argumentInstances = merge(dataSet, inMapping, argumentInstances);
        } catch (Exception e) {
            throw new N2oException(e);
        }
        return argumentInstances;
    }

    /**
     * Возвращает классы аргументов вызываемого метода, используя кэширование
     *
     * @param method параметры метода
     * @return массив классов
     */
    protected Class<?>[] takeClassesOfArguments(JavaInvocation method) {
        int argumentCount = method.getArguments() != null ? method.getArguments().length : 0;
        Class<?>[] classesOfArguments = new Class[argumentCount];
        try {
            for (int i = 0; i < argumentCount; i++) {
                classesOfArguments[i] = ClassHash.getClass(method.getArguments()[i].getClassName());
            }
        } catch (Exception e) {
            throw new N2oException("Class of argument not found", e);
        }
        return classesOfArguments;
    }

    /**
     * Меняет inMapping для type="entity",
     * "name" -> "[0].name"
     *
     * @param method    параметры метода
     * @param inMapping
     * @return innMapping вида [0].name
     */
    private Map<String, String> changeInMappingForEntity(JavaInvocation method, Map<String, String> inMapping) {
        if (method.getArguments() == null || method.getArguments().length == 0) {
            final int[] idx = {0};
            Map<String, String> newMap = new HashMap<>();
            inMapping.forEach((k, v) -> newMap.put(k, v != null ? v : String.format("[%s]", idx[0]++)));
            inMapping = newMap;
        } else {
            int entityPosition = findEntityPosition(method);
            if (entityPosition != -1) {
                // позиция entity используется для создания префикса
                String prefix = "[" + entityPosition + "].";
                for (String key : inMapping.keySet()) {
                    String value = inMapping.get(key);
                    if (value != null) {
                        if (!value.startsWith("[")) {
                            inMapping.put(key, prefix + value);
                        }
                    }
                }
            }
        }
        return inMapping;
    }

    /**
     * Находит номер позиции entity в аргументах метода
     *
     * @param method параметры метода
     * @return номер позиции entity или -1, если нет типа entity
     */
    private static int findEntityPosition(JavaInvocation method) {
        for (int i = 0; i < method.getArguments().length; i++) {
            if ((method.getArguments()[i].getType() != null) &&
                    (method.getArguments()[i].getType().equals(Argument.Type.ENTITY))) {
                return i;
            }
        }
        return -1;
    }

    private static <T> T merge(DataSet dataSet, Map<String, String> mapping, T source) {
        mapping.entrySet().stream().filter(e -> e.getValue() != null).forEach(entry -> {
            set(dataSet.get(entry.getKey()), entry.getValue(), source);
        });
        return source;
    }

    private static void set(Object val, String field, Object obj) {
        Expression expression = writeParser.parseExpression(field);
        if (val != null) expression.setValue(obj, val);
    }
}
