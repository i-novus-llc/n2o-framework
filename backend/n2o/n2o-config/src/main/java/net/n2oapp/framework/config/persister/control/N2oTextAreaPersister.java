package net.n2oapp.framework.config.persister.control;

import net.n2oapp.framework.api.metadata.control.plain.N2oTextArea;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

/**
 * User: iryabov
 * Date: 05.12.13
 * Time: 14:52
 */
@Component
public class N2oTextAreaPersister extends N2oControlXmlPersister<N2oTextArea> {
    public Element persist(N2oTextArea textArea, Namespace namespace) {
        Element textAreaElement = new Element(getElementName(), getNamespacePrefix(), getNamespaceUri());
        setControl(textAreaElement, textArea);
        setField(textAreaElement, textArea);
        setText(textAreaElement, textArea);
        return textAreaElement;
    }

    @Override
    public Class<N2oTextArea> getElementClass() {
        return N2oTextArea.class;
    }

    @Override
    public String getElementName() {
        return "text-area";
    }

}
