package net.n2oapp.framework.engine.test.source;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * AOP Advice for wrapping spring in proxy before invocation
 *
 * @author igafurov
 * @since 16.05.2017
 */
public class BeforeSpringInvocation implements MethodBeforeAdvice {

    @Override
    public void before(Method method, Object[] args, Object target)
            throws Throwable {
        System.out.println("BeforeSpringInvocation!");
    }
}
