package net.n2oapp.framework.engine.data.normalize;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.MethodInfo;
import io.github.classgraph.ScanResult;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.n2oapp.framework.engine.SpringApplicationContextProvider;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Objects.isNull;
import static org.springframework.util.StringUtils.hasText;

/**
 * Сборщик нормализующих функций
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NormalizerCollector {

    private static final String PACKAGES_PROPERTY = "n2o.engine.normalizer.packages";

    /**
     * Метод для поиска нормализующих функций и сборки в мапу<алиас-функция>
     *
     * @return Мапа нормализующих функций
     */
    public static Map<String, Method> collect() {
        Set<Method> functions = new HashSet<>();
        try (ScanResult scanResult = new ClassGraph().enableAllInfo().acceptPackages(getPackagesToScan()).enableExternalClasses().scan()) {

            for (ClassInfo classInfo : scanResult.getClassesWithAnnotation(Normalizer.class))
                functions.addAll(filterPublicStaticMethods(Arrays.asList(classInfo.loadClass().getDeclaredMethods())));

            for (ClassInfo classInfo : scanResult.getClassesWithMethodAnnotation(Normalizer.class)) {
                List<Method> annotatedMethods = classInfo.getMethodInfo()
                        .filter(methodInfo -> methodInfo.getAnnotationInfo(Normalizer.class) != null)
                        .stream().map(MethodInfo::loadClassAndGetMethod).toList();
                functions.addAll(filterPublicStaticMethods(annotatedMethods));
            }
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

    private static String[] getPackagesToScan() {
        String packagesToScanProperty = SpringApplicationContextProvider.getEnvironmentProperty(PACKAGES_PROPERTY);
        String[] packagesToScan;
        if (packagesToScanProperty != null)
            packagesToScan = packagesToScanProperty.trim().split(",");
        else
            return new String[0];

        Set<String> result = new HashSet<>();
        for (String p : packagesToScan) {
            p = p.trim();
            if (hasText(p))
                result.add(p);
        }
        return result.toArray(new String[result.size()]);
    }
}
