package net.n2oapp.framework.engine.ejb;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.dataprovider.EjbProvider;
import net.n2oapp.framework.api.metadata.dataprovider.DIProvider;
import net.n2oapp.framework.engine.data.java.ObjectLocator;
import org.springframework.stereotype.Component;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Поиск EJB бина
 */
@Component
public class EjbObjectLocator implements ObjectLocator<EjbProvider> {

    private Context initialContext;

    public void setInitialContext(Context initialContext) {
        this.initialContext = initialContext;
    }

    public EjbObjectLocator() throws NamingException {
        this.initialContext = new InitialContext();
    }

    @Override
    public Object locate(Class<?> targetClass, EjbProvider provider) {
        try {
            String ejbUrl = provider.getEjbUri() != null ? provider.getEjbUri() : getConcatUri(targetClass, provider);
            return createEjbProxy(initialContext, ejbUrl);
        } catch (NamingException | ClassCastException e) {
            throw new N2oException(e);
        }
    }

    /**
     * Concat url from ejb parameters
     *
     * @param props method parameters
     * @return ejb url
     */
    private String getConcatUri(Class<?> targetClass, EjbProvider props) {
        String beanType = "";
        if (props.getEjbStateful() != null) {
            beanType = props.getEjbStateful() ? "statefull" : "stateless";
        }
        return ((props.getEjbProtocol() != null) ? (props.getEjbProtocol() + ":/") : "") +
                ((props.getEjbApplication() != null) ? (props.getEjbApplication() + "/") : "") +
                ((props.getEjbModule() != null) ? (props.getEjbModule() + "/") : "") +
                ((props.getEjbDistinct() != null) ? (props.getEjbDistinct() + "/") : "") +
                ((props.getEjbBean() != null) ? (props.getEjbBean() + "!") : "") +
                ((targetClass != null) ? (targetClass.getName() + "?") : "") +
                beanType;
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
    public boolean match(DIProvider provider) {
        return provider instanceof EjbProvider;
    }
}
