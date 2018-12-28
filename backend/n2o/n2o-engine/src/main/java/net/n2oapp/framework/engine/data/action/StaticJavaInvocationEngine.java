package net.n2oapp.framework.engine.data.action;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.global.dao.invocation.java.JavaInvocation;
import net.n2oapp.framework.engine.util.ClassHash;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Вызов static java методов по параметрам в JavaInvocation
 */
@Component
@Deprecated
public class StaticJavaInvocationEngine extends JavaInvocationEngine<JavaInvocation> {

    @Override
    public Class<JavaInvocation> getType() {
        return JavaInvocation.class;
    }

    @Override
    public Object invoke(JavaInvocation invocation, Object[] data) {
        try {
            Class<?>[] classesOfArguments = takeClassesOfArguments(invocation);
            Class methodClass = Class.forName(invocation.getClassName());
            Method declaredMethod = methodClass.getDeclaredMethod(invocation.getMethodName(), classesOfArguments);
            return declaredMethod.invoke(null, data);
        } catch (N2oException e) {
            throw e;
        } catch (Exception e) {
            throw new N2oException(e);
        }
    }
}
