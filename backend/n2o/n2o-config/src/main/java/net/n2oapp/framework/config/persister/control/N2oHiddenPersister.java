package net.n2oapp.framework.config.persister.control;

import net.n2oapp.framework.api.metadata.control.N2oHidden;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;


/**
 * User: iryabov
 * Date: 19.11.13
 * Time: 16:51
 */
@Component
public class N2oHiddenPersister extends N2oControlXmlPersister<N2oHidden> {
    @Override
    public Element persist(N2oHidden n2o, Namespace namespace) {
        Element element = new Element(getElementName(), namespacePrefix, namespaceUri);
        Element n2oHiddenElement = new Element(getElementName(), element.getNamespace());
        setControl(n2oHiddenElement, n2o);
        setField(n2oHiddenElement, n2o);
        return n2oHiddenElement;
    }

    @Override
    public Class<N2oHidden> getElementClass() {
        return N2oHidden.class;
    }

    @Override
    public String getElementName() {
        return "hidden";
    }
}
