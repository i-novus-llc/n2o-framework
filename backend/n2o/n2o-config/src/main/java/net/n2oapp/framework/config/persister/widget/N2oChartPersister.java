package net.n2oapp.framework.config.persister.widget;

import net.n2oapp.framework.api.metadata.global.view.widget.N2oChart;
import net.n2oapp.framework.config.persister.util.PersisterJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * @author V. Alexeev.
 * @date 03.03.2016
 */
public class N2oChartPersister extends WidgetXmlPersister<N2oChart> {

    @Override
    public Element getWidget(N2oChart n2o, Namespace namespace) {
        Element rootElement = new Element(getElementName(), namespace);
        persistWidget(rootElement, n2o, namespace);
        if (n2o.getDisplay() != null) {
            Element display = new Element("display", namespace);
            display.addContent(n2o.getDisplay().toString().toLowerCase());
            rootElement.addContent(display);
        }

        PersisterJdomUtil.setElementString(rootElement, "label-field-id", n2o.getLabelFieldId());

        PersisterJdomUtil.setElementString(rootElement, "value-field-id", n2o.getValueFieldId());

        PersisterJdomUtil.setSubChild(rootElement, "values", "value", n2o.getValues(), (v, n) -> {

            Element element = new Element("value", namespace);
            PersisterJdomUtil.setAttribute(element, "field-id", v.getFieldId());
            PersisterJdomUtil.setAttribute(element, "color", v.getColor());
            return element;
        });

        return rootElement;
    }

    @Override
    public Class<N2oChart> getElementClass() {
        return N2oChart.class;
    }

    @Override
    public String getElementName() {
        return "chart";
    }
}
