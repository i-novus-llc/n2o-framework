package net.n2oapp.framework.config.persister.control;

import org.jdom.Element;
import net.n2oapp.framework.api.metadata.control.interval.N2oDateInterval;
import org.springframework.stereotype.Component;
import org.jdom.Namespace;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setAttribute;

/**
 * Сохраняет контрол интервал дат
 */
@Component
public class N2oDateIntervalPersister extends N2oControlXmlPersister<N2oDateInterval> {
    @Override
    public Element persist(N2oDateInterval n2o, Namespace namespace) {
        Element n2oDateElement = new Element(getElementName(), namespacePrefix, namespaceUri);
        setAttribute(n2oDateElement, "format", n2o.getDateFormat());
        setAttribute(n2oDateElement, "popup-placement", n2o.getPopupPlacement());
        setAttribute(n2oDateElement, "begin-default-time", n2o.getBeginDefaultTime());
        setAttribute(n2oDateElement, "end-default-time", n2o.getEndDefaultTime());
        setAttribute(n2oDateElement, "utc", n2o.getUtc());
        setControl(n2oDateElement, n2o);
        setField(n2oDateElement, n2o);
        setDefaultModel(n2oDateElement, n2o);
        return n2oDateElement;
    }


    @Override
    public Class<N2oDateInterval> getElementClass() {
        return N2oDateInterval.class;
    }

    @Override
    public String getElementName() {
        return "date-interval";
    }
}
