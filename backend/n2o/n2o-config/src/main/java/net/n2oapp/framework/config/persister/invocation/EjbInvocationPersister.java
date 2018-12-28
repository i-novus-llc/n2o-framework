package net.n2oapp.framework.config.persister.invocation;

import net.n2oapp.framework.api.metadata.global.dao.invocation.java.EjbInvocation;
import org.jdom.Element;
import org.jdom.Namespace;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setAttribute;
import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setElement;

/**
 * Persisting ejb invocation attributes
 *
 * @author igafurov
 * @since 05.05.2017
 */
public class EjbInvocationPersister extends N2oJavaInvocationPersister<EjbInvocation> {
    public EjbInvocationPersister() {
        setNamespaceUri("http://n2oapp.net/framework/config/schema/n2o-invocations-2.0");
    }

    @Override
    public Element persist(EjbInvocation entity, Namespace namespace) {
        Element root = super.persist(entity,namespace);
        Element ejb = setElement(root, "ejb");
        setAttribute(ejb, "bean", entity.getBeanId());
        setAttribute(ejb, "uri", entity.getUri());
        setAttribute(ejb, "protocol", entity.getProtocol());
        setAttribute(ejb, "application", entity.getApplication());
        setAttribute(ejb, "module", entity.getModule());
        setAttribute(ejb, "distinct", entity.getDistinct());
        setAttribute(ejb, "statefull", entity.getStatefull());
        return root;
    }

    @Override
    public Class<EjbInvocation> getElementClass() {
        return EjbInvocation.class;
    }

}
