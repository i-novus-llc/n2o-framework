package net.n2oapp.framework.engine.data.java;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.dataprovider.N2oJavaDataProvider;
import net.n2oapp.framework.api.metadata.dataprovider.SpringProvider;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.Argument;
import net.n2oapp.framework.engine.test.source.SpringInvocationTestClass;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Тестирование вызова spring метода
 */
public class SpringDataProviderEngineTest {

    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        SpringInvocationTestClass testCase = new SpringInvocationTestClass();
        applicationContext = mock(ApplicationContext.class);
        when(applicationContext.getBean("testBean")).thenReturn(testCase);
        when(applicationContext.getBean("testBean", SpringInvocationTestClass.class)).thenReturn(testCase);
        when(applicationContext.getBean(SpringInvocationTestClass.class)).thenReturn(testCase);
    }


    /**
     * Вызов spring метода без аргументов
     */
    @Test
    void testMethodWithoutArguments() {
        N2oJavaDataProvider method = new N2oJavaDataProvider();
        SpringProvider props = new SpringProvider();
        props.setSpringBean("testBean");
        method.setDiProvider(props);
        method.setMethod("methodWithoutArguments");
        Object[] dataSet = new Object[0];
        JavaDataProviderEngine javaInvocation = new JavaDataProviderEngine();
        SpringObjectLocator springObjectLocator = new SpringObjectLocator();
        springObjectLocator.setApplicationContext(applicationContext);
        javaInvocation.setLocators(Arrays.asList(springObjectLocator));
        String resultDataSet = (String) javaInvocation.invoke(method, dataSet);
        assertThat(resultDataSet, is("Invocation success"));
    }

    /**
     * Вызов spring метода с одним аргументом
     */
    @Test
    void testMethodWithOneArgument() {
        N2oJavaDataProvider method = new N2oJavaDataProvider();
        SpringProvider props = new SpringProvider();
        props.setSpringBean("testBean");
        method.setDiProvider(props);
        method.setMethod("methodWithOneArgument");
        Argument argument = new Argument();
        argument.setName("argument");
        argument.setClassName("java.lang.String");
        method.setArguments(new Argument[]{argument});
        Object[] dataSet = new Object[1];
        dataSet[0] = "1";
        JavaDataProviderEngine javaInvocation = new JavaDataProviderEngine();
        SpringObjectLocator springObjectLocator = new SpringObjectLocator();
        springObjectLocator.setApplicationContext(applicationContext);
        javaInvocation.setLocators(Arrays.asList(springObjectLocator));
        String resultDataSet = (String) javaInvocation.invoke(method, dataSet);
        assertThat(resultDataSet, is("1 invocation success"));
    }

    /**
     * Вызов spring метода с двумя аргументами
     */
    @Test
    void testMethodWithTwoArguments() {
        N2oJavaDataProvider method = new N2oJavaDataProvider();
        method.setMethod("methodWithTwoArguments");
        SpringProvider props = new SpringProvider();
        props.setSpringBean("testBean");
        method.setDiProvider(props);
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
        SpringObjectLocator springObjectLocator = new SpringObjectLocator();
        springObjectLocator.setApplicationContext(applicationContext);
        javaInvocation.setLocators(Arrays.asList(springObjectLocator));
        String resultDataSet = (String) javaInvocation.invoke(method, dataSet);
        assertThat(resultDataSet, is("Invocation success. First argument: 1, Second argument: 2"));
    }

    /**
     * Вызов spring void метода
     */
    @Test
    void testVoidMethod() {
        N2oJavaDataProvider method = new N2oJavaDataProvider();
        method.setMethod("methodVoid");
        SpringProvider props = new SpringProvider();
        props.setSpringBean("testBean");
        method.setDiProvider(props);
        Argument argument = new Argument();
        argument.setName("argument");
        argument.setClassName("java.lang.String");
        method.setArguments(new Argument[]{argument});
        Object[] dataSet = new Object[1];
        dataSet[0] = "1";
        JavaDataProviderEngine javaInvocation = new JavaDataProviderEngine();
        SpringObjectLocator springObjectLocator = new SpringObjectLocator();
        springObjectLocator.setApplicationContext(applicationContext);
        javaInvocation.setLocators(Arrays.asList(springObjectLocator));
        String resultDataSet = (String) javaInvocation.invoke(method, dataSet);
        assertThat(resultDataSet, is(nullValue()));
    }

    /**
     * Вызов spring метода с моделью в качестве аргумента
     */
    @Test
    void testMethodWithModelArgument() {
        N2oJavaDataProvider method = new N2oJavaDataProvider();
        method.setMethod("methodWithModel");
        SpringProvider props = new SpringProvider();
        props.setSpringBean("testBean");
        method.setDiProvider(props);
        Argument argument = new Argument();
        argument.setName("argument");
        argument.setClassName("net.n2oapp.framework.engine.test.source.SpringInvocationTestClass$Model");
        method.setArguments(new Argument[]{argument});
        SpringInvocationTestClass.Model model = new SpringInvocationTestClass.Model();
        model.setTestField("success");
        Object[] dataSet = new Object[1];
        dataSet[0] = model;
        JavaDataProviderEngine javaInvocation = new JavaDataProviderEngine();
        SpringObjectLocator springObjectLocator = new SpringObjectLocator();
        springObjectLocator.setApplicationContext(applicationContext);
        javaInvocation.setLocators(Arrays.asList(springObjectLocator));
        String resultDataSet = (String) javaInvocation.invoke(method, dataSet);
        assertThat(resultDataSet, is("success"));
    }

    /**
     * Вызов spring метода с ошибкой
     */
    @Test
    void testMethodWithException() {
        assertThrows(N2oException.class, ()-> {
            N2oJavaDataProvider method = new N2oJavaDataProvider();
            method.setMethod("methodWithException");
            SpringProvider props = new SpringProvider();
            props.setSpringBean("testBean");
            method.setDiProvider(props);
            Object[] dataSet = new Object[1];
            dataSet[0] = "1";
            JavaDataProviderEngine javaInvocation = new JavaDataProviderEngine();
            SpringObjectLocator springObjectLocator = new SpringObjectLocator();
            springObjectLocator.setApplicationContext(applicationContext);
            javaInvocation.setLocators(Arrays.asList(springObjectLocator));
            String resultDataSet = (String) javaInvocation.invoke(method, dataSet);
        });
    }


}