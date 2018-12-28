package net.n2oapp.framework.engine;

import net.n2oapp.framework.api.metadata.global.dao.invocation.java.EjbInvocation;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.Argument;
import net.n2oapp.framework.engine.local.ExampleServiceImpl;
import net.n2oapp.framework.engine.local.RemoteExampleServiceImpl;
import net.n2oapp.framework.engine.ejb.EjbInvocationEngine;
import org.junit.Test;

import javax.naming.InitialContext;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Testing invoking ejb method
 */
public class EjbInvocationEngineTest {

    /**
     * Check invoking ejb method define InitialContext
     *
     * @result Method will be invoking without any error
     */
    @Test
    public void testEjbInvocationEngine() throws Throwable {
        EjbInvocation method = new EjbInvocation();
        EjbInvocationEngine javaInvocation = new EjbInvocationEngine();

        InitialContext initialContext = mock(InitialContext.class);
        javaInvocation.setInitialContext(initialContext);
        ExampleServiceImpl exampleServiceImpl = new ExampleServiceImpl();
        exampleServiceImpl.setRemoteExampleService(new RemoteExampleServiceImpl());
        String ejbUrl = "java:comp/env/ejb/ExampleService";
        when(initialContext.lookup(ejbUrl)).thenReturn(exampleServiceImpl);

        method.setUri(ejbUrl);
        method.setMethodName("greet");
        Argument argument = new Argument();
        argument.setName("argument");
        argument.setClassName("java.lang.String");
        method.setArguments(new Argument[]{argument});
        Object[] dataSet = new Object[1];
        dataSet[0] = "1";
        String result = (String) javaInvocation.invoke(method, dataSet);
        assert result.equals("Hi from remote. And your message: Hi from local. Your message: 1");
    }
}
