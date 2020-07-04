package net.n2oapp.framework.boot.ejb;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.global.dao.invocation.java.EjbInvocation;
import net.n2oapp.framework.engine.data.action.JavaInvocationEngine;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.lang.reflect.Method;

/**
 * Вызов методов ejb бинов по параметрам в EjbInvocation
 * Поддерживается только singleton!
 *
 * @author igafurov
 * @since 22.05.2017
 */
@Deprecated
public class EjbInvocationEngine extends JavaInvocationEngine<EjbInvocation> {

    public Context initialContext;

    public void setInitialContext(Context initialContext) {
        this.initialContext = initialContext;
    }

    @Override
    public Object invoke(EjbInvocation invocation, Object[] data) {
        try {
            String ejbUrl = invocation.getUri() != null ? invocation.getUri() : getConcatUri(invocation);
            initialContext = initialContext != null ? initialContext : new InitialContext();
            Object objectToInvoke = createEjbProxy(initialContext, ejbUrl);
            Class<?>[] classesOfArguments = takeClassesOfArguments(invocation);
            Method declaredMethod = objectToInvoke.getClass().getDeclaredMethod(invocation.getMethodName(), classesOfArguments);
            return declaredMethod.invoke(objectToInvoke, data);
        } catch (N2oException e) {
            throw e;
        } catch (Exception e) {
            throw new N2oException(e);
        }
    }

    /**
     * Concat url from ejb parameters
     *
     * @param invocation method parameters
     * @return ejb url
     */
    private String getConcatUri(EjbInvocation invocation) {
        String beanType = "";
        if (invocation.getStatefull() != null) {
            beanType = invocation.getStatefull() == true ? "statefull" : "stateless";
        }
        StringBuilder stringBuilder = new StringBuilder()
                .append((invocation.getProtocol() != null) ? (invocation.getProtocol() + ":/") : "")
                .append((invocation.getApplication() != null) ? (invocation.getApplication() + "/") : "")
                .append((invocation.getModule() != null) ? (invocation.getModule() + "/") : "")
                .append((invocation.getDistinct() != null) ? (invocation.getDistinct() + "/") : "")
                .append((invocation.getBeanId() != null) ? (invocation.getBeanId() + "!") : "")
                .append((invocation.getClassName() != null) ? (invocation.getClassName() + "?") : "")
                .append(beanType);
        return stringBuilder.toString();
    }

    /**
     * Get a proxy for a remote EJB
     *
     * @param remotingContext remote EJB context
     * @param ejbUrl          URL of the EJB
     * @param <T>             type of the EJB remote interface
     * @return EJB proxy
     * @throws NamingException    if the name resolving fails
     * @throws ClassCastException if the EJB proxy is not of the given type
     */
    @SuppressWarnings("unchecked")
    private <T> T createEjbProxy(Context remotingContext, String ejbUrl)
            throws NamingException, ClassCastException {
        Object resolvedproxy = remotingContext.lookup(ejbUrl);
        return (T) resolvedproxy;
    }

    @Override
    public Class<EjbInvocation> getType() {
        return EjbInvocation.class;
    }
}
