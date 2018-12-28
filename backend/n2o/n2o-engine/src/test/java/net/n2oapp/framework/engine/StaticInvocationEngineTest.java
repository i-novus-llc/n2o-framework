package net.n2oapp.framework.engine;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.global.dao.invocation.java.JavaInvocation;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.Argument;
import net.n2oapp.framework.engine.data.action.StaticJavaInvocationEngine;
import net.n2oapp.framework.engine.test.source.StaticInvocationTestClass;
import org.junit.Test;

/**
 * Test invocation of different java static methods
 *
 * @author igafurov
 * @since 28.04.2017
 */
public class StaticInvocationEngineTest {

    /**
     * Invoking static java method without arguments
     *
     * @result Invoking will be passed without any errors
     */
    @Test
    public void testMethodWithoutArguments() throws Throwable {
        JavaInvocation method = new JavaInvocation();
        method.setMethodName("methodWithoutArguments");
        method.setClassName("net.n2oapp.framework.engine.test.source.StaticInvocationTestClass");
        Object[] dataSet = new Object[0];
        StaticJavaInvocationEngine javaInvocation = new StaticJavaInvocationEngine();
        String resultDataSet = (String) javaInvocation.invoke(method, dataSet);
        assert resultDataSet.equals("Invocation success");
    }

    /**
     * Invoking static java method with one argument
     *
     * @result Invoking will be passed without any errors
     */
    @Test
    public void testMethodWithOneArgument() throws Throwable {
        JavaInvocation method = new JavaInvocation();
        method.setMethodName("methodWithOneArgument");
        method.setClassName("net.n2oapp.framework.engine.test.source.StaticInvocationTestClass");
        Argument argument = new Argument();
        argument.setName("argument");
        argument.setClassName("java.lang.String");
        method.setArguments(new Argument[]{argument});
        Object[] dataSet = new Object[1];
        dataSet[0] = "1";
        StaticJavaInvocationEngine javaInvocation = new StaticJavaInvocationEngine();
        String resultDataSet = (String) javaInvocation.invoke(method, dataSet);
        assert resultDataSet.equals("1 invocation success");
    }

    /**
     * Invoking static java method with two arguments
     *
     * @result Invoking will be passed without any errors
     */
    @Test
    public void testMethodWithTwoArguments() throws Throwable {
        JavaInvocation method = new JavaInvocation();
        method.setMethodName("methodWithTwoArguments");
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
        StaticJavaInvocationEngine javaInvocation = new StaticJavaInvocationEngine();
        String resultDataSet = (String) javaInvocation.invoke(method, dataSet);
        assert resultDataSet.equals("Invocation success. First argument: 1, Second argument: 2");
    }

    /**
     * Invoking static void java method
     *
     * @result Invoking will be passed without any errors
     */
    @Test
    public void testVoidMethod() throws Throwable {
        JavaInvocation method = new JavaInvocation();
        method.setMethodName("methodVoid");
        method.setClassName("net.n2oapp.framework.engine.test.source.StaticInvocationTestClass");
        Argument argument = new Argument();
        argument.setName("argument");
        argument.setClassName("java.lang.String");
        method.setArguments(new Argument[]{argument});
        Object[] dataSet = new Object[1];
        dataSet[0] = "1";
        StaticJavaInvocationEngine javaInvocation = new StaticJavaInvocationEngine();
        Object resultDataSet = javaInvocation.invoke(method, dataSet);
        assert resultDataSet == null;
    }

    /**
     * Invoking static java method with model in arguments
     *
     * @result Invoking will be passed without any errors
     */
    @Test
    public void testMethodWithModelArgument() throws Throwable {
        JavaInvocation method = new JavaInvocation();
        method.setMethodName("methodWithModel");
        method.setClassName("net.n2oapp.framework.engine.test.source.StaticInvocationTestClass");
        Argument argument = new Argument();
        argument.setName("argument");
        argument.setClassName("net.n2oapp.framework.engine.test.source.StaticInvocationTestClass$Model");
        method.setArguments(new Argument[]{argument});
        StaticInvocationTestClass.Model model = new StaticInvocationTestClass.Model();
        model.setTestField(1);
        Object[] dataSet = new Object[1];
        dataSet[0] = model;
        StaticJavaInvocationEngine javaInvocation = new StaticJavaInvocationEngine();
        Integer resultDataSet = (Integer) javaInvocation.invoke(method, dataSet);
        assert resultDataSet.equals(1);
    }

    /**
     * Invoking static java method with throw exception
     *
     * @result Invoking will be passed with exception
     */
    @Test(expected = N2oException.class)
    public void testMethodWithException() {
        JavaInvocation method = new JavaInvocation();
        method.setMethodName("methodWithException");
        method.setClassName("net.n2oapp.framework.engine.test.source.StaticInvocationTestClass");
        Object[] dataSet = new Object[0];
        StaticJavaInvocationEngine javaInvocation = new StaticJavaInvocationEngine();
        javaInvocation.invoke(method, dataSet);
    }

    /**
     * Проверяет изменение innMapping на прямой маппинг для type="entity"
     */
    @Test
    public void testChangeInMappingForEntity() throws Throwable {
        JavaInvocation method = new JavaInvocation();
        method.setMethodName("sum");
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

        method.setArguments(new Argument[]{entityTypeArgument, primitiveTypeArgument, classTypeArgument});

        Object[] dataSet = new Object[3];
        StaticInvocationTestClass.Model model1 = new StaticInvocationTestClass.Model();
        model1.setTestField(1);
        dataSet[0] = model1;
        dataSet[1] = 2;
        StaticInvocationTestClass.Model model2 = new StaticInvocationTestClass.Model();
        model2.setTestField(7);
        dataSet[2] = model2;

        StaticJavaInvocationEngine javaInvocation = new StaticJavaInvocationEngine();
        Integer resultDataSet = (Integer) javaInvocation.invoke(method, dataSet);
        assert resultDataSet.equals(10);
    }
}
