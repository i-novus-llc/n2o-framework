package net.n2oapp.framework.engine.data.java;

import net.n2oapp.framework.api.data.ArgumentsInvocationEngine;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.data.MapInvocationEngine;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.dataprovider.DIProvider;
import net.n2oapp.framework.api.metadata.dataprovider.N2oJavaDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.Argument;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.MethodInvoker;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Провайдер данных через вызовы Java методов
 */
public class JavaDataProviderEngine implements ArgumentsInvocationEngine<N2oJavaDataProvider>,
        MapInvocationEngine<N2oJavaDataProvider> {

    private List<ObjectLocator> locators = Collections.emptyList();
    private DomainProcessor domainProcessor;
    private String javaMapping;

    private final static ExpressionParser writeParser = new SpelExpressionParser(new SpelParserConfiguration(true, true));
    private final static Set<String> primitiveTypes = Stream.of("java.lang.Boolean", "java.lang.Character", "java.lang.Byte",
            "java.lang.Short", "java.lang.Integer", "java.lang.Long", "java.lang.Float", "java.lang.Double", "java.util.Date",
            "java.math.BigDecimal").collect(Collectors.toSet());

    @Override
    public Class<? extends N2oJavaDataProvider> getType() {
        return N2oJavaDataProvider.class;
    }

    @Override
    public Object invoke(N2oJavaDataProvider invocation, Object data) {
        return invoke(invocation, mapToArgs((Map<String, Object>) data, invocation.getArguments()));
    }

    @Override
    public Object invoke(N2oJavaDataProvider invocation, Map<String, Object> data) {
        return null;
    }

    @Override
    public Object invoke(N2oJavaDataProvider dataProvider, Object[] data) {
        Class<?> targetClass = findTargetClass(dataProvider);
        Object targetObject = null;
        if (dataProvider.getDiProvider() != null)
            targetObject = match(dataProvider.getDiProvider()).locate(targetClass, dataProvider.getDiProvider());
        return invokeMethod(targetClass, dataProvider.getMethod(), targetObject, data);
    }

    private Object[] mapToArgs(Map<String, Object> dataSet, Argument[] arguments) {
        List<String> argClasses = new ArrayList<>();
        for (Argument arg : arguments) {
            argClasses.add(arg.getClassName());
        }
        Object[] instances = instantiateArguments(argClasses);
        Object[] result;
        if (ArrayUtils.isEmpty(instances))
            result = new Object[dataSet.size()];
        else
            result = instances;


        if ("map".equals(javaMapping)) {
            int idx = 0;
            for (Argument argument : arguments) {
                Map.Entry<String, Object> entry = dataSet.entrySet().stream()
                        .filter(d -> {
                            // TODO
                            String mapping = d.getKey().replaceAll("^(\\[')(.*)']$", "$1");
                            return argument.getName().equals(mapping);
                        }).findFirst().orElseThrow(() ->
                                new N2oException(String.format("Not found parameter, that could be mapping in '%s' argument ", argument.getName())));

                Expression expression = writeParser.parseExpression("[" + idx + "]");
                expression.setValue(result, dataSet.get(entry.getKey()));
                idx++;
            }
        } else
            for (Map.Entry<String, Object> entry : dataSet.entrySet()) {
                Expression expression = writeParser.parseExpression(entry.getKey());
                expression.setValue(result, entry.getValue());
            }

        for (int i = 0; i < result.length; i++) {
            if (result[i] == null && arguments[i].getDefaultValue() != null) {
                result[i] = domainProcessor.deserialize(arguments[i].getDefaultValue());
            }
        }
        return result;
    }

    private Class<?> findTargetClass(N2oJavaDataProvider dataProvider) {
        if (dataProvider.getClassName() == null)
            return null;
        Class<?> targetClass = null;
        try {
            targetClass = Class.forName(dataProvider.getClassName());
        } catch (ClassNotFoundException e) {
            throw new N2oException(e);
        }
        return targetClass;
    }

    private static Object[] instantiateArguments(List<String> arguments) {
        if (arguments == null) return null;
        Object[] argumentInstances = new Object[arguments.size()];
        for (int k = 0; k < arguments.size(); k++) {
            Class argumentClass;
            if (arguments.get(k) == null || primitiveTypes.contains(arguments.get(k))) {
                argumentInstances[k] = null;
            } else {
                try {
                    argumentClass = Class.forName(arguments.get(k));
                    argumentInstances[k] = argumentClass.newInstance();
                } catch (Exception e) {
                    throw new N2oException("Can't create instance of class " + arguments.get(k), e);
                }
            }
        }
        return argumentInstances;
    }

    private <T extends DIProvider> ObjectLocator<T> match(T provider) {
        return locators.stream().filter(l -> l.match(provider))
                .findAny().orElseThrow(() -> new IllegalArgumentException("No such data provider " + provider));
    }

    public void setLocators(List<ObjectLocator> locators) {
        this.locators = locators;
    }

    public void setJavaMapping(String javaMapping) {
        this.javaMapping = javaMapping;
    }

    public void setDomainProcessor(DomainProcessor domainProcessor) {
        this.domainProcessor = domainProcessor;
    }

    private Object invokeMethod(Class<?> targetClass, String method, Object targetObject, Object[] args) {
        MethodInvoker methodInvoker = new MethodInvoker();
        methodInvoker.setTargetClass(targetClass);
        methodInvoker.setTargetObject(targetObject);
        methodInvoker.setTargetMethod(method);
        methodInvoker.setArguments(args);

        try {
            methodInvoker.prepare();
            return methodInvoker.invoke();
        } catch (InvocationTargetException e) {
            throw new N2oException(e.getTargetException());
        } catch (NoSuchMethodException | IllegalAccessException | ClassNotFoundException e) {
            throw new N2oException(e);
        }
    }
}
