package net.n2oapp.framework.config.reader.control;

import net.n2oapp.framework.api.metadata.control.plain.N2oTextEditor;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

/**
 * @author iryabov
 * @since 23.11.2016
 */
@Component
public class N2oTextEditorReaderV1 extends N2oStandardControlReaderV1<N2oTextEditor> {
    @Override
    public N2oTextEditor read(Element element, Namespace namespace) {
        N2oTextEditor textEditor = new N2oTextEditor();
        readControlTextDefinition(element, textEditor);
        textEditor.setToolbarUrl(ReaderJdomUtil.getAttributeString(element, "config-url"));
        return textEditor;
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
