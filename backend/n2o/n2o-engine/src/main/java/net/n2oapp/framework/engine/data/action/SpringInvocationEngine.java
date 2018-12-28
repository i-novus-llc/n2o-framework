package net.n2oapp.framework.engine.data.action;

import net.n2oapp.context.StaticSpringContext;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.global.dao.invocation.java.SpringInvocation;
import org.springframework.stereotype.Component;
import org.springframework.util.MethodInvoker;

/**
 * Вызов методов spring бинов по параметрам в SpringInvocation.
 * Поддерживается только singleton!
 */
@Component
@Deprecated
public class SpringInvocationEngine extends JavaInvocationEngine<SpringInvocation> {

    @Override
    public Class<SpringInvocation> getType() {
        return SpringInvocation.class;
    }

    @Override
    public Object invoke(SpringInvocation invocation, Object[] data) {
        Object bean;
        try {
            if (invocation.getBeanId() != null && invocation.getClassName() != null) {
                bean = StaticSpringContext.getBean(invocation.getBeanId(), Class.forName(invocation.getClassName()));
            } else if (invocation.getBeanId() != null) {
                bean = StaticSpringContext.getBean(invocation.getBeanId());
            } else {
                bean = StaticSpringContext.getBean(Class.forName(invocation.getClassName()));
            }
            return invokeSpring(invocation, data, bean);
        } catch (N2oException e) {
            throw e;
        } catch (Exception e) {
            throw new N2oException(e);
        }
    }

    private Object invokeSpring(SpringInvocation method, Object data, Object objectToInvoke) {
        MethodInvoker methodInvoker = new MethodInvoker();
        methodInvoker.setTargetObject(objectToInvoke);
        methodInvoker.setTargetMethod(method.getMethodName());
        methodInvoker.setTargetClass(objectToInvoke.getClass());
        methodInvoker.setArguments((Object[]) data);
        try {
            methodInvoker.prepare();
            return methodInvoker.invoke();
        } catch (N2oException e) {
            throw e;
        } catch (Exception e) {
            throw new N2oException(e);
        }
    }
}
