package net.n2oapp.framework.engine.data.normalize;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.MethodInfo;
import io.github.classgraph.ScanResult;

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

    /**
     * Метод для поиска нормализующих функций и сборки в мапу<алиас-функция>
     *
     * @return Мапа нормализующих функций
     */
    public static Map<String, Method> collect() {
        Set<Method> functions = new HashSet<>();
        try (ScanResult scanResult = new ClassGraph().enableAllInfo().acceptPackages("*")
                .scan()) {

            for (ClassInfo classInfo : scanResult.getClassesWithAnnotation(Normalizer.class))
                functions.addAll(filterPublicStaticMethods(Arrays.asList(classInfo.loadClass().getDeclaredMethods())));

            for (ClassInfo classInfo : scanResult.getClassesWithMethodAnnotation(Normalizer.class)) {
                List<Method> annotatedMethods = classInfo.getMethodInfo().filter(methodInfo -> methodInfo.getAnnotationInfo(Normalizer.class) != null).stream().map(MethodInfo::loadClassAndGetMethod).collect(Collectors.toList());
                functions.addAll(filterPublicStaticMethods(annotatedMethods));
            }
        } catch (Exception e) {
            System.out.println(e);
            System.out.println(e.getMessage());
        }

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
