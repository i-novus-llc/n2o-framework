package net.n2oapp.framework.config.io.fieldset;

import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oPanelFieldSet;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;
import org.springframework.stereotype.Component;

/**
 *Чтение/запись панели с набором полей.
 */
@Component
public class PanelFieldsetElementIOv4 extends FieldsetElementIOv4<N2oPanelFieldSet> {

    @Override
    public void io(Element e, N2oPanelFieldSet fs, IOProcessor p) {
        super.io(e,fs,p);
        p.attribute(e,"icon", fs::getIcon, fs::setIcon);
        p.attribute(e,"class", fs::getCssClass, fs::setCssClass);
        p.attributeBoolean(e,"header", fs::getHeader, fs::setHeader);
    }

    @Override
    public String getElementName() {
        return "panel";
    }

    @Override
    public Class<N2oPanelFieldSet> getElementClass() {
        return N2oPanelFieldSet.class;
    }
}
