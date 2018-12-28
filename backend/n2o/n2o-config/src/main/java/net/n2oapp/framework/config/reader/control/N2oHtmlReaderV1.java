package net.n2oapp.framework.config.reader.control;

import net.n2oapp.framework.api.metadata.control.plain.N2oHtml;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

/**
 * @author iryabov
 * @since 01.12.2016
 */
@Component
public class N2oHtmlReaderV1 extends N2oStandardControlReaderV1<N2oHtml> {
    @Override
    public N2oHtml read(Element element, Namespace namespace) {
        N2oHtml text = new N2oHtml();
        readControlTextDefinition(element, text);
        return text;
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