package net.n2oapp.framework.config.io.control.v3;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.control.N2oButtonField;
import net.n2oapp.framework.api.metadata.global.view.action.LabelType;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ConfirmType;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.action.v2.ActionIOv2;
import net.n2oapp.framework.config.io.common.BadgeAwareIO;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись базовых свойств компонента ButtonField версии 3.0
 */

@Component
public class ButtonFieldIOv3 extends ActionFieldIOv3<N2oButtonField> implements ControlIOv3, BadgeAwareIO<N2oButtonField> {

    private Namespace actionDefaultNamespace = ActionIOv2.NAMESPACE;

    @Override
    public void io(Element e, N2oButtonField m, IOProcessor p) {
        super.io(e, m, p);

        p.attribute(e, "datasource", m::getDatasourceId, m::setDatasourceId);
        p.attributeEnum(e, "model", m::getModel, m::setModel, ReduxModel.class);
        p.attribute(e, "icon", m::getIcon, m::setIcon);
        p.attribute(e, "color", m::getColor, m::setColor);
        p.attributeBoolean(e, "validate", m::getValidate, m::setValidate);
        p.attributeArray(e, "validate-datasources", ",", m::getValidateDatasourceIds, m::setValidateDatasourceIds);
        p.attribute(e, "tooltip-position", m::getTooltipPosition, m::setTooltipPosition);
        p.attributeBoolean(e, "rounded", m::getRounded, m::setRounded);

        p.attribute(e, "confirm", m::getConfirm, m::setConfirm);
        p.attribute(e, "confirm-text", m::getConfirmText, m::setConfirmText);
        p.attributeEnum(e, "confirm-type", m::getConfirmType, m::setConfirmType, ConfirmType.class);
        p.attribute(e, "confirm-title", m::getConfirmTitle, m::setConfirmTitle);
        p.attribute(e, "confirm-ok-label", m::getConfirmOkLabel, m::setConfirmOkLabel);
        p.attribute(e, "confirm-ok-color", m::getConfirmOkColor, m::setConfirmOkColor);
        p.attribute(e, "confirm-cancel-label", m::getConfirmCancelLabel, m::setConfirmCancelLabel);
        p.attribute(e, "confirm-cancel-color", m::getConfirmCancelColor, m::setConfirmCancelColor);

        p.attributeEnum(e, "type", m::getType, m::setType, LabelType.class);
        badge(e, m, p);
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
