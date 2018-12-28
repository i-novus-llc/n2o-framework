package net.n2oapp.framework.config.persister.control;

import net.n2oapp.framework.api.metadata.control.interval.N2oTimeInterval;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

/**
 * Сохраняет контрол интервал времени в xml-файл
 */
@Component
public class N2oTimeIntervalPersister extends N2oControlXmlPersister<N2oTimeInterval> {

    @Override
    public Element persist(N2oTimeInterval entity,Namespace namespace) {
        Element element = new Element(getElementName(), Namespace.getNamespace(namespacePrefix, namespaceUri));
        setControl(element, entity);
        setField(element, entity);
        setDefaultModel(element, entity);
        return element;
    }

    @Override
    public Class<N2oTimeInterval> getElementClass() {
        return N2oTimeInterval.class;
    }

    @Override
    public String getElementName() {
        return "time-interval";
    }
}
