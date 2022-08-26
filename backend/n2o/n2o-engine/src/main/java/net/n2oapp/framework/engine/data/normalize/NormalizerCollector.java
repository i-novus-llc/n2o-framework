package net.n2oapp.framework.engine.data.normalize;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isStatic;

/**
 * Сборщик нормализующих функций
 */
public class NormalizerCollector {
    private static final String BASE_PACKAGE = ".";
    private static final Reflections ref = new Reflections(new ConfigurationBuilder().forPackages(BASE_PACKAGE));
    private static final List<Class<?>> libs = List.of(ListUtils.class, CollectionUtils.class);

    /**
     * Метод для поиска и сборки в сет нормализующих функций
     *
     * @return Сет нормализующих функций
     */
    public static Set<Method> collect() {
        Set<Method> normalizers = new HashSet<>();
        for (Class<?> normClass : ref.getTypesAnnotatedWith(Normalizer.class))
            normalizers.addAll(filterPublicStaticMethods(Arrays.asList(normClass.getDeclaredMethods())));
        normalizers.addAll(filterPublicStaticMethods(ref.getMethodsAnnotatedWith(Normalizer.class)));
        normalizers.addAll(collectFromExternalLibs());
        return normalizers;
    }

    private static Set<Method> collectFromExternalLibs() {
        return libs.stream()
                .flatMap(clazz -> filterPublicStaticMethods(Arrays.asList(clazz.getDeclaredMethods())).stream())
                .collect(Collectors.toSet());
    }

    private static Set<Method> filterPublicStaticMethods(Collection<Method> methods) {
        return methods.stream()
                .filter(method -> isPublic(method.getModifiers()) && isStatic(method.getModifiers()))
                .collect(Collectors.toSet());
    }
}
