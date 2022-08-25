package net.n2oapp.framework.engine.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NormalizerCollector {
    private static final String BASE_PACKAGE = ".";
    private static final Reflections ref = new Reflections(new ConfigurationBuilder().forPackages(BASE_PACKAGE));
    private static final List<Class<?>> libs = List.of(ListUtils.class, CollectionUtils.class);

    public static List<Method> collect() {
        List<Method> normalizers = new ArrayList<>();
        for (Class<?> normClass : ref.getTypesAnnotatedWith(Normalizer.class)) {
            normalizers.addAll(Arrays.asList(normClass.getDeclaredMethods()));//FIXME check static
        }
        normalizers.addAll(ref.getMethodsAnnotatedWith(Normalizer.class));//FIXME check static
        normalizers.addAll(collectFromExternalLibs());
        return normalizers;
    }

    private static List<Method> collectFromExternalLibs() {
        return libs.stream().flatMap(clazz -> Arrays.stream(clazz.getDeclaredMethods())).collect(Collectors.toList());
    }
}
