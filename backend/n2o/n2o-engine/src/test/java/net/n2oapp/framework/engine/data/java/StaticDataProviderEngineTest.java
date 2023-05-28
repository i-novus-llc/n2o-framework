package net.n2oapp.framework.engine.data.java;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.dataprovider.N2oJavaDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.Argument;
import net.n2oapp.framework.engine.test.source.StaticInvocationTestClass;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестирование вызова статического метода
 */
public class StaticDataProviderEngineTest {

    /**
     * Вызов статического метода без аргементов
     */
    @Test
    void testMethodWithoutArguments() {
        N2oJavaDataProvider method = new N2oJavaDataProvider();
        method.setMethod("methodWithoutArguments");
        method.setClassName("net.n2oapp.framework.engine.test.source.StaticInvocationTestClass");
        Object[] dataSet = new Object[0];
        JavaDataProviderEngine javaInvocation = new JavaDataProviderEngine();
        String resultDataSet = (String) javaInvocation.invoke(method, dataSet);
        assert resultDataSet.equals("Invocation success");
    }

    /**
     * Вызов статического метода с одним аргументом
     */
    @Test
    void testMethodWithOneArgument() {
        N2oJavaDataProvider method = new N2oJavaDataProvider();
        method.setMethod("methodWithOneArgument");
        method.setClassName("net.n2oapp.framework.engine.test.source.StaticInvocationTestClass");
        Argument argument = new Argument();
        argument.setName("argument");
        argument.setClassName("java.lang.String");
        method.setArguments(new Argument[]{argument});
        Object[] dataSet = new Object[1];
        dataSet[0] = "1";
        JavaDataProviderEngine javaInvocation = new JavaDataProviderEngine();
        String resultDataSet = (String) javaInvocation.invoke(method, dataSet);
        assert resultDataSet.equals("1 invocation success");
    }

    /**
     * Вызов статического метода с двумя аргументами
     */
    @Test
    void testMethodWithTwoArguments() {
        N2oJavaDataProvider method = new N2oJavaDataProvider();
        method.setMethod("methodWithTwoArguments");
        method.setClassName("net.n2oapp.framework.engine.test.source.StaticInvocationTestClass");
        Argument firstArgument = new Argument();
        firstArgument.setName("first");
        firstArgument.setClassName("java.lang.String");
        Argument secondArgument = new Argument();
        secondArgument.setName("2");
        secondArgument.setClassName("java.lang.Integer");
        method.setArguments(new Argument[]{firstArgument, secondArgument});
        Object[] dataSet = new Object[2];
        dataSet[0] = "1";
        dataSet[1] = 2;
        JavaDataProviderEngine javaInvocation = new JavaDataProviderEngine();
        String resultDataSet = (String) javaInvocation.invoke(method, dataSet);
        assert resultDataSet.equals("Invocation success. First argument: 1, Second argument: 2");
    }

    /**
     * Вызов статического void метода
     */
    @Test
    void testVoidMethod() {
        N2oJavaDataProvider method = new N2oJavaDataProvider();
        method.setMethod("methodVoid");
        method.setClassName("net.n2oapp.framework.engine.test.source.StaticInvocationTestClass");
        Argument argument = new Argument();
        argument.setName("argument");
        argument.setClassName("java.lang.String");
        method.setArguments(new Argument[]{argument});
        Object[] dataSet = new Object[1];
        dataSet[0] = "1";
        JavaDataProviderEngine javaInvocation = new JavaDataProviderEngine();
        Object resultDataSet = javaInvocation.invoke(method, dataSet);
        assert resultDataSet == null;
    }

    /**
     * Вызов статического метода с моделью в качестве аргумента
     */
    @Test
    void testMethodWithModelArgument() {
        N2oJavaDataProvider method = new N2oJavaDataProvider();
        method.setMethod("methodWithModel");
        method.setClassName("net.n2oapp.framework.engine.test.source.StaticInvocationTestClass");
        Argument argument = new Argument();
        argument.setName("argument");
        argument.setClassName("net.n2oapp.framework.engine.test.source.StaticInvocationTestClass$Model");
        method.setArguments(new Argument[]{argument});
        StaticInvocationTestClass.Model model = new StaticInvocationTestClass.Model();
        model.setTestField(1);
        Object[] dataSet = new Object[1];
        dataSet[0] = model;
        JavaDataProviderEngine javaInvocation = new JavaDataProviderEngine();
        Integer resultDataSet = (Integer) javaInvocation.invoke(method, dataSet);
        assert resultDataSet.equals(1);
    }

    /**
     * Вызов статического метода с ошибкой
     */
    @Test
    void testMethodWithException() {
        assertThrows(N2oException.class, () -> {
            N2oJavaDataProvider method = new N2oJavaDataProvider();
            method.setMethod("methodWithException");
            method.setClassName("net.n2oapp.framework.engine.test.source.StaticInvocationTestClass");
            Object[] dataSet = new Object[0];
            JavaDataProviderEngine javaInvocation = new JavaDataProviderEngine();
            javaInvocation.invoke(method, dataSet);
        });
    }

    /**
     * Проверяет изменение innMapping на прямой маппинг для type="entity"
     */
    @Test
    void testChangeInMappingForEntity() {
        N2oJavaDataProvider method = new N2oJavaDataProvider();
        method.setMethod("sum");
        method.setClassName("net.n2oapp.framework.engine.test.source.StaticInvocationTestClass");

        Argument entityTypeArgument = new Argument();
        entityTypeArgument.setName("entityTypeArgument");
        entityTypeArgument.setClassName("net.n2oapp.framework.engine.test.source.StaticInvocationTestClass$Model");
        entityTypeArgument.setType(Argument.Type.ENTITY);

        Argument primitiveTypeArgument = new Argument();
        primitiveTypeArgument.setName("primitiveTypeArgument");
        primitiveTypeArgument.setClassName("java.lang.Integer");
        primitiveTypeArgument.setType(Argument.Type.PRIMITIVE);

        Argument classTypeArgument = new Argument();
        classTypeArgument.setName("classTypeArgument");
        classTypeArgument.setClassName("net.n2oapp.framework.engine.test.source.StaticInvocationTestClass$Model");
        classTypeArgument.setType(Argument.Type.CLASS);

        method.setArguments(new Argument[]{entityTypeArgument, primitiveTypeArgument, primitiveTypeArgument, classTypeArgument});

        Object[] dataSet = new Object[4];
        StaticInvocationTestClass.Model model1 = new StaticInvocationTestClass.Model();
        model1.setTestField(1);
        dataSet[0] = model1;
        dataSet[1] = 2;
        dataSet[2] = 100;
        StaticInvocationTestClass.Model model2 = new StaticInvocationTestClass.Model();
        model2.setTestField(7);
        dataSet[3] = model2;

        JavaDataProviderEngine javaInvocation = new JavaDataProviderEngine();
        Integer resultDataSet = (Integer) javaInvocation.invoke(method, dataSet);
        assert resultDataSet.equals(110);
    }
}
