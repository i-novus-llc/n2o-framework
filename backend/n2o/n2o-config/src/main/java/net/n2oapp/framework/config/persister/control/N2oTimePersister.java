package net.n2oapp.framework.config.persister.control;

import net.n2oapp.framework.api.metadata.control.plain.N2oTime;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

/**
 * @author V. Alexeev.
 */
@Component
public class N2oTimePersister extends N2oControlXmlPersister<N2oTime> {

    @Override
    public Element persist(N2oTime entity,Namespace namespace) {
        Element time = new Element(getElementName(), Namespace.getNamespace(namespacePrefix, namespaceUri));
        setControl(time, entity);
        setField(time, entity);
        return time;
    }

    @Override
    public Class<N2oTime> getElementClass() {
        return N2oTime.class;
    }

    @Override
    public String getElementName() {
        return "time";
    }
}
