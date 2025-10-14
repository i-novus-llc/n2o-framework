package net.n2oapp.framework.config.io.toolbar.v2;

import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.DisableOnEmptyModelTypeEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oAbstractButton;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.common.ActionsAwareIO;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись кнопки версии 2.0
 */
@Component
public class ButtonIOv2 extends AbstractButtonIOv2<N2oButton> implements ActionsAwareIO<N2oButton>, ButtonIOAware<N2oButton> {

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
        button(e, b, p, this);
        b.adapterV2();

        p.attributeArray(e, "generate", ",", b::getGenerate, b::setGenerate);
        p.attributeEnum(e, "disable-on-empty-model", b::getDisableOnEmptyModel, b::setDisableOnEmptyModel, DisableOnEmptyModelTypeEnum.class);
        p.anyChildren(e, "dependencies", b::getDependencies, b::setDependencies, p.oneOf(N2oAbstractButton.Dependency.class)
                .add("enabling", N2oAbstractButton.EnablingDependency.class, this::enablingDependency)
                .add("visibility", N2oAbstractButton.VisibilityDependency.class, this::dependency));
    }

    private void dependency(Element e, N2oAbstractButton.Dependency t, IOProcessor p) {
        p.attribute(e, "datasource", t::getDatasource, t::setDatasource);
        p.attributeEnum(e, "model", t::getModel, t::setModel, ReduxModelEnum.class);
        p.text(e, t::getValue, t::setValue);
    }

    private void enablingDependency(Element e, N2oAbstractButton.EnablingDependency t, IOProcessor p) {
        dependency(e, t, p);
        p.attribute(e, "message", t::getMessage, t::setMessage);
    }
}
