package net.n2oapp.framework.config.io.region.v3;

import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.global.view.region.N2oScrollspyRegion;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.widget.v5.WidgetIOv5;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись региона с отслеживанием прокрутки
 */
@Component
public class ScrollspyRegionIOv3 extends AbstractRegionIOv3<N2oScrollspyRegion> {

    @Override
    public void io(Element e, N2oScrollspyRegion r, IOProcessor p) {
        super.io(e, r, p);
        p.attribute(e, "active", r::getActive, r::setActive);
        p.attribute(e, "placement", r::getPlacement, r::setPlacement);
        p.attribute(e, "title", r::getTitle, r::setTitle);
        p.attributeBoolean(e, "headlines", r::getHeadlines, r::setHeadlines);
        p.anyChildren(e, null, r::getMenu, r::setMenu, p.oneOf(N2oScrollspyRegion.AbstractMenuItem.class)
            .add("menu-item", N2oScrollspyRegion.MenuItem.class, this::menuItem)
            .add("sub-menu", N2oScrollspyRegion.SubMenuItem.class, this::subMenu));
    }

    private void menuItem(Element e, N2oScrollspyRegion.MenuItem m, IOProcessor p) {
        initItem(e, m, p);
        p.anyChildren(e, null, m::getContent, m::setContent, p.anyOf(SourceComponent.class),
                WidgetIOv5.NAMESPACE, RegionIOv3.NAMESPACE);
    }

    private void subMenu(Element e, N2oScrollspyRegion.SubMenuItem m, IOProcessor p) {
        initItem(e, m, p);
        p.anyChildren(e, null, m::getSubMenu, m::setSubMenu, p.oneOf(N2oScrollspyRegion.AbstractMenuItem.class)
            .add("menu-item", N2oScrollspyRegion.MenuItem.class, this::menuItem)
            .add("sub-menu", N2oScrollspyRegion.SubMenuItem.class, this::subMenu));
    }

    private void initItem(Element e, N2oScrollspyRegion.AbstractMenuItem m, IOProcessor p) {
        p.attribute(e, "id", m::getId, m::setId);
        p.attribute(e, "title", m::getTitle, m::setTitle);
    }

    @Override
    public Class<N2oScrollspyRegion> getElementClass() {
        return N2oScrollspyRegion.class;
    }

    @Override
    public String getElementName() {
        return "scrollspy";
    }
}
