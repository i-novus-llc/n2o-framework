package net.n2oapp.framework.config.io.widget;

import net.n2oapp.framework.api.metadata.global.view.widget.N2oHtmlWidget;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись html - виджета.
 */
@Component
public class HtmlWidgetElementIOv5 extends WidgetElementIOv5<N2oHtmlWidget> {
    private static final Logger logger = LoggerFactory.getLogger(HtmlWidgetElementIOv5.class);

    @Override
    public void io(Element e, N2oHtmlWidget m, IOProcessor p) {
        super.io(e, m, p);
        p.childrenOriginalText(e, "content", m::getHtml, m::setHtml);
        p.childAttribute(e, "content", "file", m::getFile, m::setFile);
        p.read(e, m, (el, w) -> p.childAttribute(el, "content", "url", () -> null, url -> {
            logger.warn("Атрибут 'url' элемента \"<content>\" устарел, используйте 'file'");
            if (w.getFile() == null)
                w.setFile(url);
        }));
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