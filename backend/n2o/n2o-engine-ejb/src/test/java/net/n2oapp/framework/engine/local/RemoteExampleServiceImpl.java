package net.n2oapp.framework.engine.local;

/**
 * Test class for invocation ejb method
 * Using in EjbInvocationEngineTest
 *
 * @author igafurov
 * @since 11.05.2017
 */
public class RemoteExampleServiceImpl implements RemoteExampleService {
    @Override
    public String invoke(String input) {
        return "Hi from remote. And your message: " + input;
    }
}
