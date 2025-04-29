package net.n2oapp.framework.config.io.control.v3.filters_buttons;

import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.control.filter_buttons.N2oFilterButtonField;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.common.BadgeAwareIO;
import net.n2oapp.framework.config.io.control.v3.ControlIOv3;
import net.n2oapp.framework.config.io.control.v3.FieldIOv3;
import org.jdom2.Element;

public abstract class FilterButtonFieldIOv3<T extends N2oFilterButtonField> extends FieldIOv3<T> implements ControlIOv3, BadgeAwareIO<T> {

    @Override
    public void io(Element e, T m, IOProcessor p) {
        super.io(e, m, p);
        badge(e, m, p);
        p.attribute(e, "datasource", m::getDatasourceId, m::setDatasourceId);
        p.attributeEnum(e, "model", m::getModel, m::setModel, ReduxModelEnum.class);
        p.attribute(e, "icon", m::getIcon, m::setIcon);
        p.attribute(e, "color", m::getColor, m::setColor);
        p.attribute(e, "tooltip-position", m::getTooltipPosition, m::setTooltipPosition);
        p.attributeBoolean(e, "rounded", m::getRounded, m::setRounded);
    }
}
