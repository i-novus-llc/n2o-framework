package net.n2oapp.framework.config.persister.control;

import net.n2oapp.framework.api.metadata.control.plain.N2oTextEditor;
import net.n2oapp.framework.config.persister.util.PersisterJdomUtil;
import org.jdom.Element;
import org.springframework.stereotype.Component;
import org.jdom.Namespace;

/**
 * @author iryabov
 * @since 23.11.2016
 */
@Component
public class N2oTextEditorPersister extends N2oControlXmlPersister<N2oTextEditor> {
    @Override
    public Element persist(N2oTextEditor textEditor, Namespace namespace) {
        Element element = new Element(getElementName(), namespacePrefix, namespaceUri);
        setControl(element, textEditor);
        setField(element, textEditor);
        setText(element, textEditor);
        PersisterJdomUtil.setAttribute(element, "config-url", textEditor.getToolbarUrl());
        return element;
    }

    @Override
    public Class<N2oTextEditor> getElementClass() {
        return N2oTextEditor.class;
    }

    @Override
    public String getElementName() {
        return "text-editor";
    }
}