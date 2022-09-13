package net.n2oapp.framework.engine.data.normalize;

import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Objects.isNull;

/**
 * Сборщик нормализующих функций
 */
public class NormalizerCollector {
    private static final String BASE_PACKAGE = ".";
    private static final Reflections ref = new Reflections(new ConfigurationBuilder().forPackages(BASE_PACKAGE));

    /**
     * Метод для поиска нормализующих функций и сборки в мапу<алиас-функция>
     *
     * @return Мапа нормализующих функций
     */
    public static Map<String, Method> collect() {
        Set<Method> functions = new HashSet<>();
        for (Class<?> normClass : ref.getTypesAnnotatedWith(Normalizer.class))
            functions.addAll(filterPublicStaticMethods(Arrays.asList(normClass.getDeclaredMethods())));

        functions.addAll(filterPublicStaticMethods(ref.getMethodsAnnotatedWith(Normalizer.class)));
        return functions.stream().collect(Collectors.toMap(NormalizerCollector::findAlias, Function.identity()));
    }

    private static String findAlias(Method function) {
        if (isNull(function.getAnnotation(Normalizer.class)))
            return function.getName();
        String alias = function.getAnnotation(Normalizer.class).value();
        return !alias.isBlank() ? alias : function.getName();
    }

    private static Set<Method> filterPublicStaticMethods(Collection<Method> methods) {
        return methods.stream()
                .filter(method -> isPublic(method.getModifiers()) && isStatic(method.getModifiers()))
                .collect(Collectors.toSet());
    }
}
