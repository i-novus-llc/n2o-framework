package net.n2oapp.framework.engine.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Кэширует получаемые классы
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClassHash {

    private static final Map<String, Class> classMap = new ConcurrentHashMap<>();

    /**
     * Берет класс из кэша
     *
     * @param className имя класса
     * @return класс
     */
    public static Class getClass(String className) {
        Class clazz = classMap.get(className);
        if (clazz == null) {
            try {
                clazz = Class.forName(className);
                classMap.put(className, clazz);
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException(e);
            }
        }
        return clazz;
    }
}
