package net.n2oapp.framework.config.io.control.v2.plain;

import net.n2oapp.framework.api.metadata.control.plain.N2oOutputList;
import net.n2oapp.framework.api.metadata.global.view.action.control.TargetEnum;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.meta.control.OutputList;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись компонента вывода многострочного текста
 */
@Component
public class OutputListIOv2 extends PlainFieldIOv2<N2oOutputList> {

    @Override
    public void io(Element e, N2oOutputList m, IOProcessor p) {
        super.io(e, m, p);
        p.attribute(e, "label-field-id", m::getLabelFieldId, m::setLabelFieldId);
        p.attribute(e, "href-field-id", m::getHrefFieldId, m::setHrefFieldId);
        p.attributeEnum(e, "target", m::getTarget, m::setTarget, TargetEnum.class);
        p.attributeEnum(e, "direction", m::getDirection, m::setDirection, OutputList.DirectionEnum.class);
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
