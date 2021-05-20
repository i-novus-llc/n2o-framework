package net.n2oapp.framework.engine.data.java;

import net.n2oapp.framework.api.data.ArgumentsInvocationEngine;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.dataprovider.DIProvider;
import net.n2oapp.framework.api.metadata.dataprovider.N2oJavaDataProvider;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.util.MethodInvoker;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Провайдер данных через вызовы Java методов
 */
public class JavaDataProviderEngine implements ArgumentsInvocationEngine<N2oJavaDataProvider> {

    private List<ObjectLocator> locators = Collections.emptyList();
    private String mapping;

    @Override
    public Class<? extends N2oJavaDataProvider> getType() {
        return N2oJavaDataProvider.class;
    }

    @Override
    public Object invoke(N2oJavaDataProvider dataProvider, Object[] data) {
        Class<?> targetClass = findTargetClass(dataProvider);
        Object targetObject = null;
        if (dataProvider.getDiProvider() != null)
            targetObject = match(dataProvider.getDiProvider()).locate(targetClass, dataProvider.getDiProvider());
        Object[] args = changeArgumentsOrder(dataProvider, data, targetClass);
        return invokeMethod(targetClass, dataProvider.getMethod(), targetObject, args);
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

    private Object[] changeArgumentsOrder(N2oJavaDataProvider provider, Object[] data, Class<?> targetClass) {
        if (!"map".equals(mapping) || data.length <= 1) return data;

        for (Method method : ReflectionUtils.getAllDeclaredMethods(targetClass)) {
            if (method.getName().equals(provider.getMethod()) &&
                    method.getParameterCount() == provider.getArguments().length) {
                Map<String, Class<?>> providersArgsInfo = new HashMap<>();
                Map<String, Object> dataByArgsName = new HashMap<>();
                for (int i = 0; i < provider.getArguments().length; i++) {
                    providersArgsInfo.put(provider.getArguments()[i].getName(), data[i].getClass());
                    dataByArgsName.put(provider.getArguments()[i].getName(), data[i]);
                }

                Map<String, Class<?>> methodParametersInfo = new HashMap<>();
                String[] parameterNames = new DefaultParameterNameDiscoverer().getParameterNames(method);
                for (int i = 0; i < parameterNames.length; i++)
                    methodParametersInfo.put(parameterNames[i], method.getParameterTypes()[i]);

                if (providersArgsInfo.equals(methodParametersInfo)) {
                    Object[] args = new Object[data.length];
                    for (int i = 0; i < parameterNames.length; i++)
                        args[i] = dataByArgsName.get(parameterNames[i]);

                    return args;
                }
            }
        }

        throw new N2oException(
                String.format("Not found method %s for java provider. Check the arguments naming or their types.", provider.getMethod())
        );
    }

    private <T extends DIProvider> ObjectLocator<T> match(T provider) {
        return locators.stream().filter(l -> l.match(provider))
                .findAny().orElseThrow(() -> new IllegalArgumentException("No such data provider " + provider));
    }

    public void setLocators(List<ObjectLocator> locators) {
        this.locators = locators;
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
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
