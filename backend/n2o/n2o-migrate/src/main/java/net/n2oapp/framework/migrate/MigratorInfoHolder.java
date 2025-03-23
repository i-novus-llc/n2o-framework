package net.n2oapp.framework.migrate;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Создание структуры хранения:
 * ThreadLocal используется для хранения информации в рамках одного потока.
 */

public class MigratorInfoHolder {
    /**
     * Стек для хранения текущих Source объектов
     */
    private static final ThreadLocal<Stack<Object>> currentSourceScope = ThreadLocal.withInitial(Stack::new);

    /**
     * Структура соответствий
     * ключ Object - Source объект, Ключ String - имя атрибута, Значение String - значение атрибута (настройка)
     */
    private static final ThreadLocal<Map<Object, Map<String, String>>> attributePropertiesScope = ThreadLocal.withInitial(HashMap::new);

    private MigratorInfoHolder() {
    }

    /**
     * Добавить настройку в структуру
     * текущий Object ключ берется из верхушки стека currentSourceScope
     */
    public static void addProperty(String name, String value) {
        Object currentSource = getCurrentSource();
        if (currentSource == null) return;
        Map<Object, Map<String, String>> propertiesMap = attributePropertiesScope.get();
        Map<String, String> innerMap;
        if (propertiesMap.containsKey(currentSource)) {
            innerMap = propertiesMap.get(currentSource);
            innerMap.put(name, value);
        } else {
            innerMap = new HashMap<>();
            innerMap.put(name, value);
            propertiesMap.put(currentSource, innerMap);
        }
    }

    /**
     * Получение настройки по имени атрибута
     * текущий Object ключ берется из верхушки стека currentSourceScope
     */
    public static String getProperty(String name) {
        Object currentSource = getCurrentSource();
        if (currentSource == null) return null;

        Map<String, String> properties = attributePropertiesScope.get().get(currentSource);
        return properties != null ? properties.get(name) : null;
    }

    /**
     * Добавляет текущий Source объект на вершину стека
     */
    public static void pushCurrentSource(Object source) {
        currentSourceScope.get().push(source);
    }

    /**
     * Удаляет текущий Source объект с вершины стека
     */
    public static void popCurrentSource() {
        Stack<Object> stack = currentSourceScope.get();
        if (!stack.isEmpty()) {
            stack.pop();
        }
    }

    /**
     * Получает текущий Source объект с вершины стека
     */
    private static Object getCurrentSource() {
        Stack<Object> stack = currentSourceScope.get();
        return stack.isEmpty() ? null : stack.peek();
    }

    /**
     * Очистить обе ThreadLocal переменные
     */
    public static void clear() {
        currentSourceScope.remove();
        attributePropertiesScope.remove();
    }
}