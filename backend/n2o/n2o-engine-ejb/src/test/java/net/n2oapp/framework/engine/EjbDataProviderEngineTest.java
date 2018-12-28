package net.n2oapp.framework.engine;

import net.n2oapp.framework.api.metadata.dataprovider.EjbProvider;
import net.n2oapp.framework.api.metadata.dataprovider.N2oJavaDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.Argument;
import net.n2oapp.framework.engine.data.java.JavaDataProviderEngine;
import net.n2oapp.framework.engine.ejb.EjbObjectLocator;
import net.n2oapp.framework.engine.local.ExampleServiceImpl;
import net.n2oapp.framework.engine.local.RemoteExampleServiceImpl;
import org.junit.Test;


import javax.naming.InitialContext;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Тестирование вызова ejb метода
 */
public class EjbDataProviderEngineTest {

    /**
     * Проверка вызова ejb метода с InitialContext
     */
    @Test
    public void testEjbDataProviderEngine() throws Throwable {
        N2oJavaDataProvider provider = new N2oJavaDataProvider();
        String ejbUrl = "java:comp/env/ejb/ExampleService";
        EjbProvider props = new EjbProvider();
        props.setEjbUri(ejbUrl);
        provider.setDiProvider(props);
        JavaDataProviderEngine javaInvocation = new JavaDataProviderEngine();
        InitialContext initialContext = mock(InitialContext.class);
        EjbObjectLocator ejbObjectLocator = new EjbObjectLocator();
        ejbObjectLocator.setInitialContext(initialContext);
        javaInvocation.setLocators(Arrays.asList(ejbObjectLocator));
        ExampleServiceImpl exampleServiceImpl = new ExampleServiceImpl();
        exampleServiceImpl.setRemoteExampleService(new RemoteExampleServiceImpl());
        when(initialContext.lookup(ejbUrl)).thenReturn(exampleServiceImpl);
        provider.setMethod("greet");
        Argument argument = new Argument();
        argument.setName("argument");
        argument.setClassName("java.lang.String");
        provider.setArguments(new Argument[]{argument});
        Object[] dataSet = new Object[1];
        dataSet[0] = "1";
        String result = (String) javaInvocation.invoke(provider, dataSet);
        assertThat(result, is("Hi from remote. And your message: Hi from local. Your message: 1"));
    }
}
