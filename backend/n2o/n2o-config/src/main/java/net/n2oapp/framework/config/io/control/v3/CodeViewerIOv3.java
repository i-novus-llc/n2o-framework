package net.n2oapp.framework.config.io.control.v3;

import net.n2oapp.framework.api.metadata.control.plain.CodeLanguageEnum;
import net.n2oapp.framework.api.metadata.control.plain.N2oCodeViewer;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение, запись компонента отображения кода
 */
@Component
public class CodeViewerIOv3 extends StandardFieldIOv3<N2oCodeViewer>{

    @Override
    public void io(Element e, N2oCodeViewer m, IOProcessor p) {
        super.io(e, m, p);
        p.text(e, m::getText, m::setText);
        p.attributeEnum(e, "language", m::getLanguage, m::setLanguage, CodeLanguageEnum.class);
        p.attributeEnum(e, "theme", m::getTheme, m::setTheme, N2oCodeViewer.ColorThemeEnum.class);
        p.attributeBoolean(e, "show-line-numbers", m::getShowLineNumbers, m::setShowLineNumbers);
        p.attributeInteger(e, "starting-line-number", m::getStartingLineNumber, m::setStartingLineNumber);
        p.attributeBoolean(e, "hide-buttons", m::getHideButtons, m::setHideButtons);
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
