package net.n2oapp.framework.config.persister.control;

import net.n2oapp.framework.api.metadata.control.plain.N2oPassword;
import net.n2oapp.framework.config.persister.util.PersisterJdomUtil;
import org.jdom.Element;
import org.springframework.stereotype.Component;
import org.jdom.Namespace;

/**
 * @author V. Alexeev.
 */
@Component
public class N2oPasswordPersister extends N2oControlXmlPersister<N2oPassword> {

    @Override
    public Element persist(N2oPassword password, Namespace namespace) {
        Element element = new Element(getElementName(), namespacePrefix, namespaceUri);
        setControl(element, password);
        setField(element, password);
        PersisterJdomUtil.setAttribute(element, "length", password.getLength());
        return element;
    }

    @Override
    public Class<N2oPassword> getElementClass() {
        return N2oPassword.class;
    }

    @Override
    public String getElementName() {
        return "password";
    }
}
