package net.n2oapp.framework.config.io.toolbar.v2;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ClipboardButtonDataEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oAbstractButton;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oClipboardButton;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись кнопки {@code <clipboard-button>}
 */
@Component
public class ClipboardButtonIOv2 extends AbstractButtonIOv2<N2oClipboardButton> {

    @Override
    public Class<N2oClipboardButton> getElementClass() {
        return N2oClipboardButton.class;
    }

    @Override
    public String getElementName() {
        return "clipboard-button";
    }

    @Override
    public void io(Element e, N2oClipboardButton b, IOProcessor p) {
        super.io(e, b, p);

        p.attribute(e, "data", b::getData, b::setData);
        p.attributeEnum(e, "type", b::getType, b::setType, ClipboardButtonDataEnum.class);
        p.attribute(e, "message", b::getMessage, b::setMessage);
        p.anyChildren(e, "dependencies", b::getDependencies, b::setDependencies, p.oneOf(N2oAbstractButton.Dependency.class)
                .add("enabling", N2oAbstractButton.EnablingDependency.class, this::enablingDependency)
                .add("visibility", N2oAbstractButton.VisibilityDependency.class, this::visibilityDependency));
    }

    private void enablingDependency(Element e, N2oAbstractButton.EnablingDependency t, IOProcessor p) {
        visibilityDependency(e, t, p);
        p.attribute(e, "message", t::getMessage, t::setMessage);
    }

    private void visibilityDependency(Element e, N2oAbstractButton.Dependency t, IOProcessor p) {
        p.attribute(e, "datasource", t::getDatasource, t::setDatasource);
        p.attributeEnum(e, "model", t::getModel, t::setModel, ReduxModel.class);
        p.text(e, t::getValue, t::setValue);
    }

}