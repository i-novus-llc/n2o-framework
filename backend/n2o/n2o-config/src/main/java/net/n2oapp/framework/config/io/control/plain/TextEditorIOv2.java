package net.n2oapp.framework.config.io.control.plain;

import net.n2oapp.framework.api.metadata.control.plain.N2oTextEditor;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;
import org.springframework.stereotype.Component;

@Component
public class TextEditorIOv2 extends PlainFieldIOv2<N2oTextEditor> {

    @Override
    public void io(Element e, N2oTextEditor m, IOProcessor p) {
        super.io(e, m, p);
        p.attribute(e, "toolbar-url", m::getToolbarUrl, m::setToolbarUrl);
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
