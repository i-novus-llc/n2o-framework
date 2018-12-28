package net.n2oapp.framework.config.persister.control;

import net.n2oapp.framework.api.metadata.control.interval.N2oInputInterval;
import org.jdom.Element;
import org.jdom.Namespace;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setAttribute;

/**
 * Сохраняет input-interval в xml-файл
 */
public class N2oInputIntervalPersister extends N2oControlXmlPersister<N2oInputInterval> {

    @Override
    public Element persist(N2oInputInterval field, Namespace namespace) {
        Element element = new Element(getElementName(), Namespace.getNamespace(namespacePrefix, namespaceUri));
        setControl(element, field);
        setField(element, field);
        setDefaultModel(element, field);
        setAttribute(element, "max", field.getMax());
        setAttribute(element, "min", field.getMin());
        setAttribute(element, "step", field.getStep());
        return element;
    }

    @Override
    public Class<N2oInputInterval> getElementClass() {
        return N2oInputInterval.class;
    }

    @Override
    public String getElementName() {
        return "input-interval";
    }
}
