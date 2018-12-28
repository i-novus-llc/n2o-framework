package net.n2oapp.framework.engine.local;

import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Test class for invocation ejb method
 * Using in EjbInvocationEngineTest
 *
 * @author igafurov
 * @since 11.05.2017
 */
@Stateless
public class ExampleServiceImpl implements ExampleService {

    @EJB
    private RemoteExampleService remoteExampleService;

    public void setRemoteExampleService(RemoteExampleService remoteExampleService) {
        this.remoteExampleService = remoteExampleService;
    }

    @Override
    public String greet(String argument) {
        return remoteExampleService.invoke("Hi from local. Your message: " + argument);
    }
}
