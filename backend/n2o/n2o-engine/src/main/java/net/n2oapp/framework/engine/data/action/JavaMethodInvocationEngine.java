package net.n2oapp.framework.engine.data.action;

import net.n2oapp.framework.api.data.ArgumentsInvocationEngine;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oJavaMethod;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.MethodInvoker;

import java.lang.reflect.InvocationTargetException;

/**
 * Выполнение java метода. Старая реализация, оставлена для поддержки обратной совместимости
 */
@Deprecated
@Component
public class JavaMethodInvocationEngine implements ArgumentsInvocationEngine<N2oJavaMethod>, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public Object invoke(N2oJavaMethod invocation, Object[] data) {
        String beanName = invocation.getBean();
        String methodName = invocation.getName();
        Integer sizeArguments = invocation.getArguments() != null ? invocation.getArguments().length : 0;
        String[] arguments = new String[sizeArguments];
        for (int i = 0; i < sizeArguments; i++) {
            arguments[i] = invocation.getArguments()[i].getClassName();
        }
        try {
            if (invocation.getRmi() != null) {
                throw new UnsupportedOperationException("rmi not suported since 7.0");
            } else {
                return invoke(beanName, methodName, data);
            }
        } catch (Exception e) {
            if (e.getCause() instanceof N2oException) {
                throw (N2oException) e.getCause();
            } else
                throw e;
        }
    }

    @Override
    public Class<N2oJavaMethod> getType() {
        return N2oJavaMethod.class;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    private Object invoke(String beanName, String methodName, Object[] args) {
        MethodInvoker invoker = new MethodInvoker();
        Object bean = applicationContext.getBean(beanName);
        invoker.setTargetObject(bean);
        invoker.setTargetClass(bean.getClass());
        invoker.setTargetMethod(methodName);
        invoker.setArguments(args);
        Object result;
        try {
            invoker.prepare();
            result = invoker.invoke();
        } catch (InvocationTargetException e) {
            throw new N2oException(beanName + methodName, e.getTargetException());
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException e) {
            throw new N2oException(beanName + methodName, e);
        }
        return result;
    }
}
