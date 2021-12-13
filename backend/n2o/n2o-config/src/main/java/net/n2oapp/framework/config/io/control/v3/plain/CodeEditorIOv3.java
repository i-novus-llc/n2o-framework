package net.n2oapp.framework.config.io.control.v3.plain;

import net.n2oapp.framework.api.metadata.control.plain.CodeLanguageEnum;
import net.n2oapp.framework.api.metadata.control.plain.N2oCodeEditor;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись компонента редактирования кода версии 3.0
 */
@Component
public class CodeEditorIOv3 extends PlainFieldIOv3<N2oCodeEditor> {

    @Override
    public void io(Element e, N2oCodeEditor m, IOProcessor p) {
        super.io(e, m, p);
        p.attributeEnum(e, "language", m::getLanguage, m::setLanguage, CodeLanguageEnum.class);
        p.attributeInteger(e, "min-lines", m::getMinLines, m::setMinLines);
        p.attributeInteger(e, "max-lines", m::getMaxLines, m::setMaxLines);
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
