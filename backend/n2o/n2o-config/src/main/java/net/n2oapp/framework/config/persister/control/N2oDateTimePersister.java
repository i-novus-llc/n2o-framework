package net.n2oapp.framework.config.persister.control;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.control.plain.N2oDatePicker;
import net.n2oapp.framework.config.persister.util.PersisterJdomUtil;
import org.jdom.Element;
import org.springframework.stereotype.Component;
import org.jdom.Namespace;

/**
 * @author iryabov
 * @since 11.11.2016
 */
@Component
public class N2oDateTimePersister extends N2oControlXmlPersister<N2oDatePicker> {
    @Override
    public Element persist(N2oDatePicker n2o, Namespace namespace) {
        Element n2oDateElement = new Element(getElementName(), namespacePrefix, namespaceUri);
        PersisterJdomUtil.setAttribute(n2oDateElement, "format", n2o.getDateFormat());
        PersisterJdomUtil.setAttribute(n2oDateElement, "default-time", n2o.getDefaultTime());
        PersisterJdomUtil.setAttribute(n2oDateElement, "max", n2o.getMax());
        PersisterJdomUtil.setAttribute(n2oDateElement, "min", n2o.getMin());
        PersisterJdomUtil.setAttribute(n2oDateElement, "utc", n2o.getUtc());
        setControl(n2oDateElement, n2o);
        setField(n2oDateElement, n2o);
        return n2oDateElement;
    }

    @Override
    public Class<N2oDatePicker> getElementClass() {
        return N2oDatePicker.class;
    }

    @Override
    public String getElementName() {
        return "date-time";
    }
}
