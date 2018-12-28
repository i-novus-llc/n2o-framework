package net.n2oapp.framework.config.persister.invocation;

import org.jdom.Element;
import net.n2oapp.framework.api.metadata.persister.ElementPersister;
import net.n2oapp.framework.config.persister.util.PersisterJdomUtil;
import org.jdom.Namespace;

/**
* User: operehod
* Date: 29.01.2015
* Time: 14:09
*/
public class ParamPersister implements ElementPersister<String> {
    protected String namespaceUri;
    protected String namespacePrefix;

    public ParamPersister(String namespaceUri, String namespacePrefix) {
        this.namespaceUri = namespaceUri;
        this.namespacePrefix = namespacePrefix;
    }

    public String getNamespaceUri() {
        return namespaceUri;
    }

    public String getNamespacePrefix() {
        return namespacePrefix;
    }

    @Override
    public Element persist(String entity, Namespace namespace) {
        Element element = new Element(getNamespacePrefix(), getNamespaceUri());
        PersisterJdomUtil.setAttribute(element, "param-name", entity);
        return element;
    }
}
