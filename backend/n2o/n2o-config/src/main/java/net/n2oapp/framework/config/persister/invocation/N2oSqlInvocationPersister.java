package net.n2oapp.framework.config.persister.invocation;

import org.jdom.Element;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oSqlQuery;
import net.n2oapp.framework.config.persister.util.PersisterJdomUtil;
import org.springframework.stereotype.Component;
import org.jdom.Namespace;

/**
 * User: iryabov
 * Date: 21.12.13
 * Time: 14:51
 */
@Component
public class N2oSqlInvocationPersister extends N2oInvocationPersister<N2oSqlQuery> {
    public N2oSqlInvocationPersister() {
        setNamespaceUri("http://n2oapp.net/framework/config/schema/n2o-invocations-2.0");
    }

    @Override

    public Element persist(N2oSqlQuery entity, Namespace namespace) {
        Element element = new Element(getElementName(), getNamespacePrefix(), getNamespaceUri());
        PersisterJdomUtil.setAttribute(element, "data-source", entity.getDataSource());
        element.setText(entity.getQuery());
        return element;
    }

    @Override
    public Class<N2oSqlQuery> getElementClass() {
        return N2oSqlQuery.class;
    }

    @Override
    public String getElementName() {
        return "sql";
    }
}
