package net.n2oapp.framework.config.io.control.v3;

import net.n2oapp.framework.api.metadata.control.N2oMarkdown;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись базовых свойств компонента Markdowm версии 3.0
 */

@Component
public class MarkdownFieldIOv3 extends FieldIOv3<N2oMarkdown> implements ControlIOv3 {

    @Override
    public void io(Element e, N2oMarkdown m, IOProcessor p) {
        super.io(e, m, p);
        p.attributeArray(e, "actions", ",",  m::getActionIds, m::setActionIds);
        p.text(e, m::getContent, m::setContent);
    }

    @Override
    public Class<N2oMarkdown> getElementClass() {
        return N2oMarkdown.class;
    }

    @Override
    public String getElementName() {
        return "markdown";
    }
}
