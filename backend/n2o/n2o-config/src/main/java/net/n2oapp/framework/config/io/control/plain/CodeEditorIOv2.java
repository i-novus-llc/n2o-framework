package net.n2oapp.framework.config.io.control.plain;

import net.n2oapp.framework.api.metadata.control.plain.N2oCodeEditor;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;
import org.springframework.stereotype.Component;

@Component
public class CodeEditorIOv2 extends PlainFieldIOv2<N2oCodeEditor> {

    @Override
    public void io(Element e, N2oCodeEditor m, IOProcessor p) {
        super.io(e, m, p);
        p.attributeEnum(e, "language", m::getLanguage, m::setLanguage, N2oCodeEditor.Language.class);
        p.attribute(e, "height", m::getHeight, m::setHeight);
        p.attributeInteger(e, "rows", m::getRows, m::setRows);
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
