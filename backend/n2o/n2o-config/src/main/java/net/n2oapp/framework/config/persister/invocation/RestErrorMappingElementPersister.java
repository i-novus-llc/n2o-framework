package net.n2oapp.framework.config.persister.invocation;

import org.jdom.Element;
import net.n2oapp.framework.api.metadata.global.dao.RestErrorMapping;
import net.n2oapp.framework.api.metadata.persister.ElementPersister;
import net.n2oapp.framework.config.persister.util.PersisterJdomUtil;
import org.jdom.Namespace;

/**
 * User: operehod
 * Date: 29.01.2015
 * Time: 14:09
 */
public class RestErrorMappingElementPersister implements ElementPersister<RestErrorMapping> {
    private String namespaceUri;
    private String namespacePrefix;


    public RestErrorMappingElementPersister(String namespaceUri, String namespacePrefix) {
        this.namespaceUri = namespaceUri;
        this.namespacePrefix = namespacePrefix;
    }

    @Override
    public Element persist(RestErrorMapping entity, Namespace namespace) {
        Element element = new Element(namespacePrefix, namespaceUri);
        PersisterJdomUtil.setChild(
                element, "message",
                entity.getMessage(), new ParamPersister(namespaceUri, namespacePrefix));
        PersisterJdomUtil.setChild(
                element, "detailed-message",
                entity.getDetailedMessage(), new ParamPersister(namespaceUri, namespacePrefix));
        PersisterJdomUtil.setChild(
                element, "stack-trace",
                entity.getStackTrace(), new ParamPersister(namespaceUri, namespacePrefix));
        return element;
    }
}
