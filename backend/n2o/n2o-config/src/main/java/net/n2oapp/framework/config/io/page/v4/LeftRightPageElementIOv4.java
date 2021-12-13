package net.n2oapp.framework.config.io.page.v4;

import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.global.view.page.N2oLeftRightPage;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.widget.v5.WidgetIOv5;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись страницы c двумя регионами версии 4.0
 */
@Component
public class LeftRightPageElementIOv4 extends BasePageElementIOv4<N2oLeftRightPage> {

    @Override
    public void io(Element e, N2oLeftRightPage m, IOProcessor p) {
        super.io(e, m, p);
        p.anyChildren(e, "left", m::getLeft, m::setLeft, p.anyOf(SourceComponent.class),
                getRegionDefaultNamespace(), WidgetIOv5.NAMESPACE);
        p.childAttribute(e, "left", "width", m::getLeftWidth, m::setLeftWidth);

        p.anyChildren(e, "right", m::getRight, m::setRight, p.anyOf(SourceComponent.class),
                getRegionDefaultNamespace(), WidgetIOv5.NAMESPACE);
        p.childAttribute(e, "right", "width", m::getRightWidth, m::setRightWidth);
    }

    @Override
    public Class<N2oLeftRightPage> getElementClass() {
        return N2oLeftRightPage.class;
    }

    @Override
    public N2oLeftRightPage newInstance(Element element) {
        return new N2oLeftRightPage();
    }

    @Override
    public String getElementName() {
        return "left-right-page";
    }
}
