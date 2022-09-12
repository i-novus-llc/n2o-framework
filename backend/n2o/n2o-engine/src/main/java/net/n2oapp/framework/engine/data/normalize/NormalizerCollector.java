package net.n2oapp.framework.engine.data.normalize;

import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
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
    public static Set<Method> collect() {
        Set<Method> normalizers = new HashSet<>();
        for (Class<?> normClass : ref.getTypesAnnotatedWith(Normalizer.class))
            normalizers.addAll(filterPublicStaticMethods(Arrays.asList(normClass.getDeclaredMethods())));
        normalizers.addAll(filterPublicStaticMethods(ref.getMethodsAnnotatedWith(Normalizer.class)));
        return normalizers;
    }

    private static Set<Method> filterPublicStaticMethods(Collection<Method> methods) {
        return methods.stream()
                .filter(method -> isPublic(method.getModifiers()) && isStatic(method.getModifiers()))
                .collect(Collectors.toSet());
    }
}
