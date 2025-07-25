package net.n2oapp.framework.config.io.widget.v4;

import net.n2oapp.framework.api.metadata.global.view.widget.N2oHtmlWidget;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись html - виджета.
 * */
@Component
public class HtmlWidgetElementIOv4 extends WidgetElementIOv4<N2oHtmlWidget> {

    @Override
    public void io(Element e, N2oHtmlWidget m, IOProcessor p) {
        super.io(e, m, p);
        p.childAttribute(e, "content", "url", m::getUrl, m::setUrl);
        p.childrenOriginalText(e, "content", m::getHtml, m::setHtml);
        p.merge(m, getElementName());
    }

    @Override
    public String getElementName() {
        return "html";
    }

    @Override
    public Class<N2oHtmlWidget> getElementClass() {
        return N2oHtmlWidget.class;
    }
}
