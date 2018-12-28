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
public interface ExampleService {

    /**
     * Return a special string with input argument
     *
     * @param argument input
     * @return "Hi from local. Your message: $argument"
     */
    public String greet(String argument);
}
