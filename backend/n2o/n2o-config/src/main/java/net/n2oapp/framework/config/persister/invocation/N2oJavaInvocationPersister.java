package net.n2oapp.framework.config.persister.invocation;

import net.n2oapp.framework.api.metadata.global.dao.invocation.java.JavaInvocation;
import net.n2oapp.framework.config.persister.util.PersisterJdomUtil;
import org.jdom.Element;
import org.springframework.stereotype.Component;
import org.jdom.Namespace;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.*;

/**
 * Persisting action attributes considering n2o-invocations-2.0.xsd
 *
 * @author igafurov
 * @since 05.05.2017
 */
@Component
public class N2oJavaInvocationPersister<T extends JavaInvocation> extends N2oInvocationPersister<T> {
    public N2oJavaInvocationPersister() {
        setNamespaceUri("http://n2oapp.net/framework/config/schema/n2o-invocations-2.0");
    }

    @Override
    public Element persist(T entity, Namespace namespace) {
        Element element = new Element(getElementName(), getNamespacePrefix(), getNamespaceUri());
        setAttribute(element, "class", entity.getClassName());
        setAttribute(element, "method", entity.getMethodName());

        PersisterJdomUtil.setChildren(element, "arguments", "argument", entity.getArguments(), (s,n) -> {
            Element argument = new Element("argument");
            PersisterJdomUtil.setAttribute(argument, "class", s.getClassName());
            PersisterJdomUtil.setAttribute(argument, "name", s.getName());
            PersisterJdomUtil.setAttribute(argument, "type", s.getType());
            return argument;
        });
        return element;
    }

    @Override
    public Class<T> getElementClass() {
        return (Class<T>) JavaInvocation.class;
    }

    @Override
    public String getElementName() {
        return "java";
    }
}
