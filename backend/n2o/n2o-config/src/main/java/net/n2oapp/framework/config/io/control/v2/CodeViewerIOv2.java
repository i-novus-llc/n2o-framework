package net.n2oapp.framework.config.io.control.v2;

import net.n2oapp.framework.api.metadata.control.plain.CodeLanguageEnum;
import net.n2oapp.framework.api.metadata.control.plain.N2oCodeViewer;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

@Component
public class CodeViewerIOv2 extends StandardFieldIOv2<N2oCodeViewer>{

    @Override
    public void io(Element e, N2oCodeViewer m, IOProcessor p) {
        super.io(e, m, p);
        p.text(e, m::getText, m::setText);
        p.attributeEnum(e, "language", m::getLanguage, m::setLanguage, CodeLanguageEnum.class);
        p.attributeEnum(e, "theme", m::getTheme, m::setTheme, N2oCodeViewer.ColorTheme.class);
        p.attributeBoolean(e, "show-line-numbers", m::getShowLineNumbers, m::setShowLineNumbers);
        p.attributeInteger(e, "starting-line-number", m::getStartingLineNumber, m::setStartingLineNumber);
        p.attributeBoolean(e, "hide-buttons", m::getHideButtons, m::setHideButtons);
        p.attributeBoolean(e, "hide-overflow", m::getHideOverflow, m::setHideOverflow);
    }

    @Override
    public Class<N2oCodeViewer> getElementClass() {
        return N2oCodeViewer.class;
    }

    @Override
    public String getElementName() {
        return "code";
    }
}
