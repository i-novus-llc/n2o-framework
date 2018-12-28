package net.n2oapp.framework.engine;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.global.dao.invocation.java.SpringInvocation;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.Argument;
import net.n2oapp.framework.engine.data.action.SpringInvocationEngine;
import net.n2oapp.framework.engine.test.source.SpringInvocationTestClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test invocation of different spring methods
 *
 * @author igafurov
 * @since 10.05.2017
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/net/n2oapp/framework/engine/test-engine-context.xml"})
public class SpringInvocationEngineTest {

    @Before
    public void setUp() throws Exception {

    }

    /**
     * Invoking spring method without arguments
     *
     * @result Invoking will be passed without any errors
     */
    @Test
    public void testMethodWithoutArguments() throws Throwable {
        SpringInvocation method = new SpringInvocation();
        method.setBeanId("testSpringInvocationProxy");
        method.setMethodName("methodWithoutArguments");
        Object[] dataSet = new Object[0];
        SpringInvocationEngine javaInvocation = new SpringInvocationEngine();
        String resultDataSet = (String) javaInvocation.invoke(method, dataSet);
        assert resultDataSet.equals("Invocation success");
    }

    /**
     * Invoking spring method with one argument
     *
     * @result Invoking will be passed without any errors
     */
    @Test
    public void testMethodWithOneArgument() throws Throwable {
        SpringInvocation method = new SpringInvocation();
        method.setMethodName("methodWithOneArgument");
        //Check invoke by class name without bean name
        method.setBeanId("testSpringInvocationProxy");
        Argument argument = new Argument();
        argument.setName("argument");
        argument.setClassName("java.lang.String");
        method.setArguments(new Argument[]{argument});
        Object[] dataSet = new Object[1];
        dataSet[0] = "1";
        SpringInvocationEngine javaInvocation = new SpringInvocationEngine();
        String resultDataSet = (String) javaInvocation.invoke(method, dataSet);
        assert resultDataSet.equals("1 invocation success");
    }

    /**
     * Invoking spring method with two arguments
     *
     * @result Invoking will be passed without any errors
     */
    @Test
    public void testMethodWithTwoArguments() throws Throwable {
        SpringInvocation method = new SpringInvocation();
        method.setMethodName("methodWithTwoArguments");
        method.setBeanId("testSpringInvocationProxy");
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
        SpringInvocationEngine javaInvocation = new SpringInvocationEngine();
        String resultDataSet = (String) javaInvocation.invoke(method, dataSet);
        assert resultDataSet.equals("Invocation success. First argument: 1, Second argument: 2");
    }

    /**
     * Invoking spring void method
     *
     * @result Invoking will be passed without any errors
     */
    @Test
    public void testVoidMethod() throws Throwable {
        SpringInvocation method = new SpringInvocation();
        method.setMethodName("methodVoid");
        method.setBeanId("testSpringInvocationProxy");
        Argument argument = new Argument();
        argument.setName("argument");
        argument.setClassName("java.lang.String");
        method.setArguments(new Argument[]{argument});
        Object[] dataSet = new Object[1];
        dataSet[0] = "1";
        SpringInvocationEngine javaInvocation = new SpringInvocationEngine();
        Object resultDataSet = javaInvocation.invoke(method, dataSet);
        assert resultDataSet == null;
    }

    /**
     * Invoking spring method with model in arguments
     *
     * @result Invoking will be passed without any errors
     */
    @Test
    public void testMethodWithModelArgument() throws Throwable {
        SpringInvocation method = new SpringInvocation();
        method.setMethodName("methodWithModel");
        method.setBeanId("testSpringInvocationProxy");
        Argument argument = new Argument();
        argument.setName("argument");
        argument.setClassName("net.n2oapp.framework.engine.test.source.SpringInvocationTestClass$Model");
        method.setArguments(new Argument[]{argument});
        SpringInvocationTestClass.Model model = new SpringInvocationTestClass.Model();
        model.setTestField("success");
        Object[] dataSet = new Object[1];
        dataSet[0] = model;
        SpringInvocationEngine javaInvocation = new SpringInvocationEngine();
        String resultDataSet = (String) javaInvocation.invoke(method, dataSet);
        assert resultDataSet.equals("success");
    }

    /**
     * Invoking spring method with throw exception
     *
     * @result Invoking will be passed with exception
     */
    @Test(expected = N2oException.class)
    public void testMethodWithException() {
        SpringInvocation method = new SpringInvocation();
        method.setMethodName("methodWithException");
        method.setBeanId("testSpringInvocationProxy");
        Object[] dataSet = new Object[1];
        dataSet[0] = "1";
        SpringInvocationEngine javaInvocation = new SpringInvocationEngine();
        javaInvocation.invoke(method, dataSet);
    }
}
