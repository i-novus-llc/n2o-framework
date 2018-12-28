package net.n2oapp.framework.config.persister.invocation;

import net.n2oapp.framework.api.metadata.global.dao.invocation.java.SpringInvocation;
import org.jdom.Element;
import org.jdom.Namespace;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setAttribute;
import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setElement;

/**
 * Persisting spring invocation attributes
 *
 * @author igafurov
 * @since 05.05.2017
 */
public class SpringInvocationPersister extends N2oJavaInvocationPersister<SpringInvocation> {
    public SpringInvocationPersister() {
        setNamespaceUri("http://n2oapp.net/framework/config/schema/n2o-invocations-2.0");
    }

    @Override
    public Element persist(SpringInvocation entity, Namespace namespace) {
        Element root = super.persist(entity,namespace);
        Element spring = setElement(root, "spring");
        setAttribute(spring, "bean", entity.getBeanId());
        return root;
    }

    @Override
    public Class<SpringInvocation> getElementClass() {
        return SpringInvocation.class;
    }
}
