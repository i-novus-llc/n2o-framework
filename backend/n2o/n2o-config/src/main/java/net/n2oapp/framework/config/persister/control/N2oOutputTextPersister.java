package net.n2oapp.framework.config.persister.control;

import org.jdom.Element;
import net.n2oapp.framework.api.metadata.control.plain.N2oOutputText;
import org.springframework.stereotype.Component;
import org.jdom.Namespace;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setAttribute;

/**
 * User: iryabov
 * Date: 19.11.13
 * Time: 16:51
 */
@Component
public class N2oOutputTextPersister extends N2oControlXmlPersister<N2oOutputText> {
    @Override
    public Element persist(N2oOutputText n2o, Namespace namespace) {
        Element n2oHiddenElement = new Element(getElementName(), namespacePrefix, namespaceUri);
        setControl(n2oHiddenElement, n2o);
        setField(n2oHiddenElement, n2o);
        setAttribute(n2oHiddenElement, "type", n2o.getType());
        setAttribute(n2oHiddenElement, "icon", n2o.getIcon());
        setAttribute(n2oHiddenElement, "position", n2o.getPosition());
        return n2oHiddenElement;
    }

    @Override
    public Class<N2oOutputText> getElementClass() {
        return N2oOutputText.class;
    }

    @Override
    public String getElementName() {
        return "output-text";
    }
}
