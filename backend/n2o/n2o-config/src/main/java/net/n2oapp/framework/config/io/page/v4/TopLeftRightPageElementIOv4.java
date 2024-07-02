package net.n2oapp.framework.config.io.page.v4;

import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.global.view.page.N2oTopLeftRightPage;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.widget.v5.WidgetIOv5;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Чтение\запись страницы c тремя регионами версии 4.0
 */
@Component
public class TopLeftRightPageElementIOv4 extends BasePageElementIOv4<N2oTopLeftRightPage> {

    @Override
    public void io(Element e, N2oTopLeftRightPage m, IOProcessor p) {
        super.io(e, m, p);
        p.attributeBoolean(e, "scroll-top-button", m::getScrollTopButton, m::setScrollTopButton);
        region(e, "top", m::getTop, m::setTop, m.getTopOptions(), p);
        region(e, "left", m::getLeft, m::setLeft, m.getLeftOptions(), p);
        region(e, "right", m::getRight, m::setRight, m.getRightOptions(), p);
    }

    private void region(Element e, String name,
                        Supplier<SourceComponent[]> itemsGetter, Consumer<SourceComponent[]> itemsSetter,
                        N2oTopLeftRightPage.RegionOptions regionsOptions, IOProcessor p) {
        p.anyChildren(e, name, itemsGetter, itemsSetter,
                p.anyOf(SourceComponent.class), getRegionDefaultNamespace(), WidgetIOv5.NAMESPACE);
        p.childAttribute(e, name, "width", regionsOptions::getWidth, regionsOptions::setWidth);
        p.childAttributeBoolean(e, name, "fixed", regionsOptions::getFixed, regionsOptions::setFixed);
        p.childAttributeInteger(e, name, "offset", regionsOptions::getOffset, regionsOptions::setOffset);
    }

    @Override
    public Class<N2oTopLeftRightPage> getElementClass() {
        return N2oTopLeftRightPage.class;
    }

    @Override
    public N2oTopLeftRightPage newInstance(Element element) {
        return new N2oTopLeftRightPage();
    }

    @Override
    public String getElementName() {
        return "top-left-right-page";
    }
}
