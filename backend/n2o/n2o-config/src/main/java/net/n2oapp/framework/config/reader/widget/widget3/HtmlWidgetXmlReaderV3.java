package net.n2oapp.framework.config.reader.widget.widget3;

import net.n2oapp.framework.api.metadata.global.view.widget.N2oHtmlWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.config.reader.tools.PropertiesReaderV1;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeString;
import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getElementString;

@Component
public class HtmlWidgetXmlReaderV3 extends WidgetBaseXmlReaderV3<N2oWidget> {
    @Override
    public String getElementName() {
        return "html";
    }

    @Override
    public N2oWidget read(Element element, Namespace namespace) {
        N2oHtmlWidget htmlWidget = new N2oHtmlWidget();
        htmlWidget.setName(getElementString(element, "name"));
        htmlWidget.setObjectId(getElementString(element, "object-id"));
        htmlWidget.setNamespaceUri(namespace.getURI());
        return htmlWidget;
    }

    @Override
    public Class<N2oWidget> getElementClass() {
        return N2oWidget.class;
    }
}
