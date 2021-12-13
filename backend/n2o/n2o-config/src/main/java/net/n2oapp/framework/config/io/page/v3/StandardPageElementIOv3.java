package net.n2oapp.framework.config.io.page.v3;


import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.global.view.page.N2oStandardPage;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.widget.v4.WidgetIOv4;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись стандартной страницы версии 3.0
 */
@Component
public class StandardPageElementIOv3 extends BasePageElementIOv3<N2oStandardPage> {

    @Override
    public void io(Element e, N2oStandardPage m, IOProcessor p) {
        super.io(e, m, p);
        p.anyChildren(e, "regions", m::getItems, m::setItems, p.anyOf(SourceComponent.class),
                getRegionDefaultNamespace(), WidgetIOv4.NAMESPACE);
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
