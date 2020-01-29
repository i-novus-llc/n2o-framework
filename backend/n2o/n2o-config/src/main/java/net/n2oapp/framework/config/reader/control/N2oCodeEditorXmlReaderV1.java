package net.n2oapp.framework.config.reader.control;

import net.n2oapp.framework.api.metadata.control.plain.CodeLanguageEnum;
import org.jdom.Element;
import org.jdom.Namespace;
import net.n2oapp.framework.api.metadata.control.plain.N2oCodeEditor;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.springframework.stereotype.Component;

@Component
public class N2oCodeEditorXmlReaderV1 extends N2oStandardControlReaderV1<N2oCodeEditor> {
    @Override
    public N2oCodeEditor read(Element element, Namespace namespace) {
        N2oCodeEditor codeEditor = new N2oCodeEditor();
        readControlTextDefinition(element, codeEditor);
        codeEditor.setLanguage(ReaderJdomUtil.getAttributeEnum(element, "language", CodeLanguageEnum.class));
        return codeEditor;
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
