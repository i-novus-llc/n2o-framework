package net.n2oapp.framework.config.io.toolbar.v2;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ConfirmType;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.DisableOnEmptyModelType;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.common.ActionsAwareIO;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись кнопки версии 2.0
 */
@Component
public class ButtonIOv2 extends AbstractButtonIOv2<N2oButton> implements ActionsAwareIO<N2oButton> {
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
        p.attributeBoolean(e, "validate", b::getValidate, b::setValidate);
        p.attributeArray(e, "validate-datasources", ",", b::getValidateDatasourceIds, b::setValidateDatasourceIds);
        p.attributeBoolean(e, "rounded", b::getRounded, b::setRounded);

        p.attribute(e, "confirm", b::getConfirm, b::setConfirm);
        p.attribute(e, "confirm-text", b::getConfirmText, b::setConfirmText);
        p.attributeEnum(e, "confirm-type", b::getConfirmType, b::setConfirmType, ConfirmType.class);
        p.attribute(e, "confirm-title", b::getConfirmTitle, b::setConfirmTitle);
        p.attribute(e, "confirm-ok-label", b::getConfirmOkLabel, b::setConfirmOkLabel);
        p.attribute(e, "confirm-ok-color", b::getConfirmOkColor, b::setConfirmOkColor);
        p.attribute(e, "confirm-cancel-label", b::getConfirmCancelLabel, b::setConfirmCancelLabel);
        p.attribute(e, "confirm-cancel-color", b::getConfirmCancelColor, b::setConfirmCancelColor);

        p.attributeEnum(e, "disable-on-empty-model", b::getDisableOnEmptyModel, b::setDisableOnEmptyModel, DisableOnEmptyModelType.class);
        p.anyChildren(e, "dependencies", b::getDependencies, b::setDependencies, p.oneOf(N2oButton.Dependency.class)
                .add("enabling", N2oButton.EnablingDependency.class, this::enablingDependency)
                .add("visibility", N2oButton.VisibilityDependency.class, this::dependency));
        action(e, b, p, "dependencies");
    }

    private void dependency(Element e, N2oButton.Dependency t, IOProcessor p) {
        p.attribute(e, "datasource", t::getDatasource, t::setDatasource);
        p.attributeEnum(e, "model", t::getModel, t::setModel, ReduxModel.class);
        p.text(e, t::getValue, t::setValue);
    }

    private void enablingDependency(Element e, N2oButton.EnablingDependency t, IOProcessor p) {
        dependency(e, t, p);
        p.attribute(e, "message", t::getMessage, t::setMessage);
    }
}
