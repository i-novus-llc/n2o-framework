package net.n2oapp.framework.config.reader.widget.widget3;

import net.n2oapp.framework.api.metadata.global.view.widget.N2oChart;
import net.n2oapp.framework.api.metadata.global.view.widget.chart.Display;
import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oChartValue;
import net.n2oapp.framework.api.metadata.reader.NamespaceReaderFactory;
import net.n2oapp.framework.api.metadata.reader.TypedElementReader;
import net.n2oapp.framework.api.metadata.reader.ElementReaderFactory;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeString;
import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getElementEnum;
import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getElementString;

@Component
public class N2oChartReaderV3 extends WidgetBaseXmlReaderV3 {

    @Override
    public String getElementName() {
        return "chart";
    }

    @Override
    public N2oChart read(Element element, Namespace namespace) {
        N2oChart chart = new N2oChart();
        readWidgetDefinition(element, element.getNamespace(), chart);
        String refId = getAttributeString(element, "ref-id");
        if (refId != null) {
            chart.setRefId(refId);
            chart.setId(refId);
            return chart;
        }
        getAbstractChartDefinition(element, namespace, chart, readerFactory);
        return chart;
    }

    private void getAbstractChartDefinition(Element element, Namespace namespace, N2oChart n2oChart,
                                                   NamespaceReaderFactory extensionReaderFactory) {

        n2oChart.setLabelFieldId(getElementString(element, "label-field-id"));
        n2oChart.setValueFieldId(getElementString(element, "value-field-id"));
        n2oChart.setDisplay(getElementEnum(element, "display", Display.class));

        Element values = element.getChild("values", namespace);

        if(values != null) {
            n2oChart.setValues(ReaderJdomUtil.getChilds(values, namespace, "value", new TypedElementReader<N2oChartValue>() {
                @Override
                public String getElementName() {
                    return "value";
                }

                @Override
                public N2oChartValue read(Element element) {
                    String fieldId = getAttributeString(element, "field-id");
                    String color = getAttributeString(element, "color");
                    return new N2oChartValue(fieldId, color);
                }

                @Override
                public Class<N2oChartValue> getElementClass() {
                    return N2oChartValue.class;
                }
            }));
        }
        readWidgetDefinition(element, namespace, n2oChart);
    }

    @Override
    public Class getElementClass() {
        return N2oChart.class;
    }
}
