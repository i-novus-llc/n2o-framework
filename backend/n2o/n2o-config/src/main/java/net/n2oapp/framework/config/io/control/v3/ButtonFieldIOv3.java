package net.n2oapp.framework.config.io.control.v3;

import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.control.N2oButtonField;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.common.BadgeAwareIO;
import net.n2oapp.framework.config.io.toolbar.v2.ButtonIOAware;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись базовых свойств компонента ButtonField версии 3.0
 */

@Component
public class ButtonFieldIOv3 extends ActionFieldIOv3<N2oButtonField> implements ControlIOv3, BadgeAwareIO<N2oButtonField>, ButtonIOAware<N2oButtonField> {

    @Override
    public void io(Element e, N2oButtonField m, IOProcessor p) {
        super.io(e, m, p);
        button(e, m, p, this);
        m.adapterV3();
        badge(e, m, p);

        p.attribute(e, "datasource", m::getDatasourceId, m::setDatasourceId);
        p.attributeEnum(e, "model", m::getModel, m::setModel, ReduxModelEnum.class);
        p.attribute(e, "icon", m::getIcon, m::setIcon);
        p.attribute(e, "color", m::getColor, m::setColor);
        p.attribute(e, "tooltip-position", m::getTooltipPosition, m::setTooltipPosition);
    }

    @Override
    public Class<N2oButtonField> getElementClass() {
        return N2oButtonField.class;
    }

    @Override
    public String getElementName() {
        return "button";
    }
}
