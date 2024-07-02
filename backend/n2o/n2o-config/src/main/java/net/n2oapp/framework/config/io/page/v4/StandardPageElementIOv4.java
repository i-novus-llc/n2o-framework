package net.n2oapp.framework.config.io.page.v4;


import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.global.view.page.N2oStandardPage;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.widget.v5.WidgetIOv5;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись стандартной страницы версии 4.0
 */
@Component
public class StandardPageElementIOv4 extends BasePageElementIOv4<N2oStandardPage> {

    @Override
    public void io(Element e, N2oStandardPage m, IOProcessor p) {
        super.io(e, m, p);
        p.anyChildren(e, "regions", m::getItems, m::setItems,
                p.anyOf(SourceComponent.class), getRegionDefaultNamespace(), WidgetIOv5.NAMESPACE);
    }

    @Override
    public Class<N2oStandardPage> getElementClass() {
        return N2oStandardPage.class;
    }

    @Override
    public N2oStandardPage newInstance(Element element) {
        return new N2oStandardPage();
    }

    @Override
    public String getElementName() {
        return "page";
    }
}
