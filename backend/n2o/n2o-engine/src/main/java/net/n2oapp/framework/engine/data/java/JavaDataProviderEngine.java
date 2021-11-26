package net.n2oapp.framework.engine.data.java;

import net.n2oapp.framework.api.data.ArgumentsInvocationEngine;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.exception.N2oUserException;
import net.n2oapp.framework.api.metadata.dataprovider.DIProvider;
import net.n2oapp.framework.api.metadata.dataprovider.N2oJavaDataProvider;
import net.n2oapp.framework.api.metadata.dataprovider.DIProvider;
import org.springframework.util.MethodInvoker;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

/**
 * Провайдер данных через вызовы Java методов
 */
public class JavaDataProviderEngine implements ArgumentsInvocationEngine<N2oJavaDataProvider> {

    private List<ObjectLocator> locators = Collections.emptyList();

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
        return invokeMethod(targetClass, dataProvider.getMethod(), targetObject, data);
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


    private <T extends DIProvider> ObjectLocator<T> match(T provider) {
        return locators.stream().filter(l -> l.match(provider))
                .findAny().orElseThrow(() -> new IllegalArgumentException("No such data provider " + provider));
    }

    public void setLocators(List<ObjectLocator> locators) {
        this.locators = locators;
    }

//    /**
//     * Возвращает классы аргументов вызываемого метода, используя кэширование
//     *
//     * @param arguments параметры метода
//     * @return массив классов
//     */
//    protected Class<?>[] takeClassesOfArguments(Argument[] arguments) {
//        int argumentCount = arguments != null ? arguments.length : 0;
//        Class<?>[] classesOfArguments = new Class[argumentCount];
//        try {
//            for (int i = 0; i < argumentCount; i++) {
//                classesOfArguments[i] = ClassHash.getClass(arguments[i].getClassName());
//            }
//        } catch (Exception e) {
//            throw new N2oException("Class of argument not found", e);
//        }
//        return classesOfArguments;
//    }

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
            if (e.getTargetException() instanceof N2oException)
                throw (N2oException)e.getTargetException();
            throw new N2oException(e.getTargetException());
        }
        catch (NoSuchMethodException | IllegalAccessException | ClassNotFoundException e) {
            throw new N2oException(e);
        }
    }

}
