package net.n2oapp.framework.config.io.region.v3;

import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.global.view.region.N2oTabsRegion;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.widget.v5.WidgetIOv5;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись региона в виде вкладок версии 3.0
 */
@Component
public class TabsRegionIOv3 extends AbstractRegionIOv3<N2oTabsRegion> {

    @Override
    public void io(Element e, N2oTabsRegion r, IOProcessor p) {
        super.io(e, r, p);
        p.attributeBoolean(e, "always-refresh", r::getAlwaysRefresh, r::setAlwaysRefresh);
        p.attributeBoolean(e, "lazy", r::getLazy, r::setLazy);
        p.attribute(e, "active-param", r::getActiveParam, r::setActiveParam);
        p.attributeBoolean(e, "routable", r::getRoutable, r::setRoutable);
        p.attributeBoolean(e, "hide-single-tab", r::getHideSingleTab, r::setHideSingleTab);
        p.attribute(e, "max-height", r::getMaxHeight, r::setMaxHeight);
        p.attributeBoolean(e, "scrollbar", r::getScrollbar, r::setScrollbar);
        p.children(e, null, "tab", r::getTabs, r::setTabs, N2oTabsRegion.Tab::new, this::tabs);
        p.anyAttributes(e, r::getExtAttributes, r::setExtAttributes);
    }

    private void tabs(Element e, N2oTabsRegion.Tab t, IOProcessor p) {
        p.attribute(e, "id", t::getId, t::setId);
        p.attribute(e, "name", t::getName, t::setName);
        p.anyChildren(e, null, t::getContent, t::setContent, p.anyOf(SourceComponent.class),
                WidgetIOv5.NAMESPACE, RegionIOv3.NAMESPACE);
        p.anyAttributes(e, t::getExtAttributes, t::setExtAttributes);
    }

    @Override
    public String getElementName() {
        return "tabs";
    }

    @Override
    public Class<N2oTabsRegion> getElementClass() {
        return N2oTabsRegion.class;
    }
}
