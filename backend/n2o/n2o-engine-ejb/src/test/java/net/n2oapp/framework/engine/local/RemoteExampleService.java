package net.n2oapp.framework.engine.local;

import javax.ejb.Local;

/**
 * Test class for invocation ejb method
 * Using in EjbInvocationEngineTest
 *
 * @author igafurov
 * @since 11.05.2017
 */
@Local
public interface RemoteExampleService {

    /**
     * Return a special string with input argument
     *
     * @param argument input
     * @return "Hi from remote. And your message: $argument"
     */
    public String invoke(String argument);
}
