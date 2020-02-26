package net.n2oapp.framework.config.io.page;


import net.n2oapp.framework.api.metadata.global.view.page.N2oStandardPage;
import net.n2oapp.framework.api.metadata.global.view.region.N2oRegion;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись страницы версии 2.0
 */
@Component
public class StandardPageElementIOv2 extends BasePageElementIOv2<N2oStandardPage> {

    @Override
    public void io(Element e, N2oStandardPage m, IOProcessor p) {
        super.io(e, m, p);
        p.anyChildren(e, "regions", m::getRegions, m::setRegions, p.anyOf(N2oRegion.class), getRegionDefaultNamespace());
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
