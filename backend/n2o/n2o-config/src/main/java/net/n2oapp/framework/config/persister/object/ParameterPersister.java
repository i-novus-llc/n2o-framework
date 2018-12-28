package net.n2oapp.framework.config.persister.object;

/*
 /* @author enuzhdina 
 /* @since 23.07.2015
 */

import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.persister.AbstractElementPersister;
import net.n2oapp.framework.config.persister.util.PersisterJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;

public class ParameterPersister extends AbstractElementPersister<N2oObject.Parameter> {
    public ParameterPersister(Namespace namespace) {
        setNamespace(namespace);
    }

    @Override
    public Element persist(N2oObject.Parameter entity, Namespace namespace) {
        Element element = new Element("param", getNamespace());
        PersisterJdomUtil.setAttribute(element, "name", entity.getId());
        PersisterJdomUtil.setAttribute(element, "value", entity.getDefaultValue());
        PersisterJdomUtil.setAttribute(element, "mapping", entity.getMapping());
        PersisterJdomUtil.setAttribute(element, "domain", entity.getDomain());
        PersisterJdomUtil.setAttribute(element, "required", entity.getRequired());
        PersisterJdomUtil.setAttribute(element, "normalizer", entity.getNormalize());
        return element;
    }
}
