package net.n2oapp.framework.config.persister.control;

import org.jdom.Element;
import net.n2oapp.framework.api.metadata.control.plain.N2oCodeEditor;
import net.n2oapp.framework.config.persister.util.PersisterJdomUtil;
import org.springframework.stereotype.Component;
import org.jdom.Namespace;

/**
 * User: iryabov
 * Date: 05.12.13
 * Time: 14:53
 */
@Component
public class N2oCodeEditorPersister extends N2oControlXmlPersister<N2oCodeEditor> {
    @Override
    public Element persist(N2oCodeEditor codeEditor, Namespace namespace) {
        Element element = new Element(getElementName(), namespacePrefix, namespaceUri);
        setControl(element, codeEditor);
        setField(element, codeEditor);
        setText(element, codeEditor);
        PersisterJdomUtil.setAttribute(element, "language", codeEditor.getLanguage());
        return element;
    }

    @Override
    public Class<N2oCodeEditor> getElementClass() {
        return N2oCodeEditor.class;
    }

    @Override
    public String getElementName() {
        return "code-editor";
    }
}