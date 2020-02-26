package net.n2oapp.framework.config.io.toolbar;

import net.n2oapp.framework.api.metadata.global.view.action.LabelType;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись кнопки
 */
@Component
public class ButtonIO extends AbstractMenuItemIO<N2oButton> {
    @Override
    public Class<N2oButton> getElementClass() {
        return N2oButton.class;
    }

    @Override
    public String getElementName() {
        return "button";
    }

    @Override
    public void io(Element e, N2oButton b, IOProcessor p) {
        super.io(e, b, p);
        p.attribute(e, "src", b::getSrc, b::setSrc);
        p.attributeEnum(e, "type", b::getType, b::setType, LabelType.class);
    }
}
