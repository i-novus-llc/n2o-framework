package net.n2oapp.framework.config.persister.control;

import net.n2oapp.framework.api.metadata.control.N2oDebug;
import net.n2oapp.framework.config.persister.util.PersisterJdomUtil;
import org.jdom.Element;
import org.springframework.stereotype.Component;
import org.jdom.Namespace;

/**
 * @author iryabov
 * @since 09.11.2016
 */
@Component
public class N2oDebugPersister extends N2oControlXmlPersister<N2oDebug> {
    @Override
    public Element persist(N2oDebug control, Namespace namespace) {
        Element element = PersisterJdomUtil.setElement(getElementName(), namespacePrefix, namespaceUri);
        setControl(element, control);
        setField(element, control);
        return element;
    }

    @Override
    public Class<N2oDebug> getElementClass() {
        return N2oDebug.class;
    }

    @Override
    public String getElementName() {
        return "debug";
    }
}
