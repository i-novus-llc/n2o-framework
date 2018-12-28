package net.n2oapp.framework.config.persister.control;

import net.n2oapp.framework.api.metadata.control.plain.N2oInputText;
import net.n2oapp.framework.config.persister.util.PersisterJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

/**
 * User: iryabov
 * Date: 19.11.13
 * Time: 16:50
 */
@Component
public class N2oInputTextPersister extends N2oControlXmlPersister<N2oInputText> {
    public Element persist(N2oInputText n2oInputText, Namespace namespace) {
        Element inputTextElement = new Element(getElementName(), getNamespacePrefix(), getNamespaceUri());
        setControl(inputTextElement, n2oInputText);
        setField(inputTextElement, n2oInputText);
        if (n2oInputText.getLength() != null)
            inputTextElement.setAttribute("length", n2oInputText.getLength().toString());
        PersisterJdomUtil.setAttribute(inputTextElement, "max", n2oInputText.getMax());
        PersisterJdomUtil.setAttribute(inputTextElement, "min", n2oInputText.getMin());
        PersisterJdomUtil.setAttribute(inputTextElement, "step", n2oInputText.getStep());
        return inputTextElement;
    }

    @Override
    public Class<N2oInputText> getElementClass() {
        return ((Class<N2oInputText>) (N2oInputText.class));
    }

    @Override
    public String getElementName() {
        return "input-text";
    }

}
