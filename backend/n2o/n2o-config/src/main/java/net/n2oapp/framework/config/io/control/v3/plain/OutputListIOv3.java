package net.n2oapp.framework.config.io.control.v3.plain;

import net.n2oapp.framework.api.metadata.control.plain.N2oOutputList;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.meta.control.OutputList;
import net.n2oapp.framework.config.io.control.plain.PlainFieldIOv2;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись компонента вывода многострочного текста версии 3.0
 */
@Component
public class OutputListIOv3 extends PlainFieldIOv3<N2oOutputList> {

    @Override
    public void io(Element e, N2oOutputList m, IOProcessor p) {
        super.io(e, m, p);
        p.attribute(e, "label-field-id", m::getLabelFieldId, m::setLabelFieldId);
        p.attribute(e, "href-field-id", m::getHrefFieldId, m::setHrefFieldId);
        p.attributeEnum(e, "target", m::getTarget, m::setTarget, Target.class);
        p.attributeEnum(e, "direction", m::getDirection, m::setDirection, OutputList.Direction.class);
        p.attribute(e, "separator", m::getSeparator, m::setSeparator);
    }

    @Override
    public Class<N2oOutputList> getElementClass() {
        return N2oOutputList.class;
    }

    @Override
    public String getElementName() {
        return "output-list";
    }
}
