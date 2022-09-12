package net.n2oapp.framework.engine.data.normalize;

import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isStatic;

/**
 * Сборщик нормализующих функций
 */
public class NormalizerCollector {
    private static final String BASE_PACKAGE = ".";
    private static final Reflections ref = new Reflections(new ConfigurationBuilder().forPackages(BASE_PACKAGE));

    /**
     * Метод для поиска и сборки в сет нормализующих функций
     *
     * @return Сет нормализующих функций
     */
    public static Map<String, Method> collect() {
        Map<String, Method> normalizers = new HashMap<>();
        for (Class<?> normClass : ref.getTypesAnnotatedWith(Normalizer.class))
            normalizers.putAll(filterPublicStaticMethods(Arrays.asList(normClass.getDeclaredMethods()))
                    .stream()
                    .collect(Collectors.toMap(Method::getName, Function.identity())));
        //normalizers.addAll(filterPublicStaticMethods(ref.getMethodsAnnotatedWith(Normalizer.class)));FIXME
        return normalizers;
    }

    private static Set<Method> filterPublicStaticMethods(Collection<Method> methods) {
        return methods.stream()
                .filter(method -> isPublic(method.getModifiers()) && isStatic(method.getModifiers()))
                .collect(Collectors.toSet());
    }
}
