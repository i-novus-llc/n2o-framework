package net.n2oapp.framework.config.persister.control;

import net.n2oapp.framework.api.metadata.control.plain.N2oMaskedInput;
import net.n2oapp.framework.config.persister.util.PersisterJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

/**
 * User: iryabov
 * Date: 12.12.13
 * Time: 13:09
 */
@Component
public class N2oMaskedInputPersister extends N2oControlXmlPersister<N2oMaskedInput> {
    public Element persist(N2oMaskedInput n2o, Namespace namespace) {
        Element element = new Element(getElementName(), getNamespacePrefix(), getNamespaceUri());
        setControl(element, n2o);
        setField(element, n2o);
        PersisterJdomUtil.setAttribute(element, "mask", n2o.getMask());
        return element;
    }

    @Override
    public Class<N2oMaskedInput> getElementClass() {
        return N2oMaskedInput.class;
    }

    @Override
    public String getElementName() {
        return "masked-input";
    }

}
