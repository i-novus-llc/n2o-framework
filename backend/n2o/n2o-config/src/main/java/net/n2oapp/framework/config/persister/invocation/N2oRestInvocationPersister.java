package net.n2oapp.framework.config.persister.invocation;

import org.jdom.Element;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oRestInvocation;
import net.n2oapp.framework.config.persister.util.PersisterJdomUtil;
import org.springframework.stereotype.Component;
import org.jdom.Namespace;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.*;

/**
 * User: operhod
 * Date: 20.01.14
 * Time: 13:06
 */
@Component
public class N2oRestInvocationPersister extends N2oInvocationPersister<N2oRestInvocation> {
    public N2oRestInvocationPersister() {
        setNamespaceUri("http://n2oapp.net/framework/config/schema/n2o-invocations-2.0");
    }

    @Override
    public Element persist(N2oRestInvocation entity,Namespace namespace) {
        Element element = new Element(getElementName(), getNamespacePrefix(), getNamespaceUri());
        setAttribute(element, "method", entity.getMethod());
        setAttribute(element, "date-format", entity.getDateFormat());
        setElementString(element, "query", entity.getQuery());
        if (entity.getProxyHost() != null) setAttribute(element, "proxy-host", entity.getProxyHost());
        if (entity.getDateFormat() != null)
            setAttribute(element, "date-format", entity.getDateFormat());
        if (entity.getProxyPort() != null)
            setAttribute(element, "proxy-port", entity.getProxyPort().toString());
        if (entity.getErrorMapping() != null) {
            setChild(element, "error-mapping", entity.getErrorMapping(), new RestErrorMappingElementPersister(getNamespaceUri(), getNamespacePrefix()));
        }
        return element;
    }

    @Override
    public Class<N2oRestInvocation> getElementClass() {
        return N2oRestInvocation.class;
    }


    @Override
    public String getElementName() {
        return "rest";
    }
}
