package net.n2oapp.framework.config.persister.control;

import net.n2oapp.framework.api.metadata.control.plain.N2oHtml;
import net.n2oapp.framework.config.persister.util.PersisterJdomUtil;
import org.jdom.Element;
import org.springframework.stereotype.Component;
import org.jdom.Namespace;

/**
 * @author iryabov
 * @since 01.12.2016
 */
@Component
public class N2oHtmlPersister extends N2oControlXmlPersister<N2oHtml> {
    @Override
    public Element persist(N2oHtml text, Namespace namespace) {
        Element element = new Element(getElementName(), namespacePrefix, namespaceUri);
        setControl(element, text);
        setField(element, text);
        setText(element, text);
        return element;
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