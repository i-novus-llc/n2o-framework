package net.n2oapp.framework.config.io.toolbar;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.DisableOnEmptyModelType;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.toolbar.v2.ButtonIOAware;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись кнопки
 */
@Component
public class ButtonIO extends AbstractButtonIO<N2oButton> implements ButtonIOAware<N2oButton> {

    @Override
    public void io(Element e, N2oButton b, IOProcessor p) {
        super.io(e, b, p);
        button(e, b, p, actionDefaultNamespace);

        p.attribute(e, "visible", b::getVisible, b::setVisible);
        p.attribute(e, "enabled", b::getEnabled, b::setEnabled);
        p.attributeEnum(e, "disable-on-empty-model", b::getDisableOnEmptyModel, b::setDisableOnEmptyModel, DisableOnEmptyModelType.class);
        p.anyChildren(e, "dependencies", b::getDependencies, b::setDependencies, p.oneOf(N2oButton.Dependency.class)
                .add("enabling", N2oButton.EnablingDependency.class, this::enablingDependency)
                .add("visibility", N2oButton.VisibilityDependency.class, this::dependency));
    }

    @Override
    public Class<N2oButton> getElementClass() {
        return N2oButton.class;
    }

    @Override
    public String getElementName() {
        return "button";
    }

    private void dependency(Element e, N2oButton.Dependency t, IOProcessor p) {
        p.attribute(e, "ref-widget-id", t::getRefWidgetId, t::setRefWidgetId);
        p.attributeEnum(e, "ref-model", t::getModel, t::setModel, ReduxModel.class);
        p.text(e, t::getValue, t::setValue);
    }

    private void enablingDependency(Element e, N2oButton.EnablingDependency t, IOProcessor p) {
        dependency(e, t, p);
        p.attribute(e, "message", t::getMessage, t::setMessage);
    }
}
