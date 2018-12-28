package net.n2oapp.framework.config.io.control.plain;

import net.n2oapp.framework.api.metadata.control.plain.N2oHtml;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;
import org.springframework.stereotype.Component;

@Component
public class HtmlIOv2 extends PlainFieldIOv2<N2oHtml> {

    @Override
    public void io(Element e, N2oHtml m, IOProcessor p) {
        super.io(e, m, p);
        p.attribute(e, "height", m::getHeight, m::setHeight);
        p.attributeInteger(e, "rows", m::getRows, m::setRows);
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
