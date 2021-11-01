package net.n2oapp.framework.config.io.control.v2.plain;

import net.n2oapp.framework.api.metadata.control.plain.N2oHtml;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись компонента вывода html
 */
@Component
public class HtmlIOv2 extends PlainFieldIOv2<N2oHtml> {

    @Override
    public void io(Element e, N2oHtml m, IOProcessor p) {
        super.io(e, m, p);
    }

    @Override
    public Class<N2oHtml> getElementClass() {
        return N2oHtml.class;
    }

    @Override
    public String getElementName() {
        return "html";
    }
}
