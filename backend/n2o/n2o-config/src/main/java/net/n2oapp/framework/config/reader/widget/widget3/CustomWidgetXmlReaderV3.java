package net.n2oapp.framework.config.reader.widget.widget3;

import net.n2oapp.framework.api.metadata.global.view.widget.N2oCustomWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.reader.ElementReaderFactory;
import net.n2oapp.framework.api.metadata.reader.NamespaceReaderFactory;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;


/**
 * Считывание переопредленного виджета версии 2.0
 */
@Component
public class CustomWidgetXmlReaderV3 extends WidgetBaseXmlReaderV3<N2oWidget> {
    @Override
    public N2oWidget read(Element element, Namespace namespace) {
        N2oCustomWidget n2oCustomWidget = new N2oCustomWidget();
        readRef(element, n2oCustomWidget);
        getCustomDefinition(element, namespace, n2oCustomWidget, readerFactory);
        return n2oCustomWidget;
    }

    private void getCustomDefinition(Element element, Namespace namespace, N2oCustomWidget n2oCustomWidget,
                                     NamespaceReaderFactory extensionReaderFactory) {
        readWidgetDefinition(element, namespace, n2oCustomWidget);
    }


    @Override
    public Class<N2oWidget> getElementClass() {
        return N2oWidget.class;
    }

    @Override
    public String getElementName() {
        return "custom";
    }
}
