package net.n2oapp.framework.config.io.page;

import net.n2oapp.framework.api.metadata.global.view.page.N2oSearchablePage;
import net.n2oapp.framework.api.metadata.global.view.region.N2oRegion;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.config.io.region.RegionIOv1;
import net.n2oapp.framework.config.io.toolbar.ToolbarIO;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись страницы с полем поиска
 */
@Component
public class SearchablePageElementIOv2 implements NamespaceIO<N2oSearchablePage> {
    private Namespace regionDefaultNamespace = RegionIOv1.NAMESPACE;
    private Namespace pageDefaultNamespace = PageIOv2.NAMESPACE;

    @Override
    public void io(Element e, N2oSearchablePage m, IOProcessor p) {
        p.attribute(e, "name", m::getName, m::setName);
        p.attribute(e, "object-id", m::getObjectId, m::setObjectId);
        p.attribute(e, "layout", m::getLayout, m::setLayout);
        p.attribute(e, "route", m::getRoute, m::setRoute);
        p.attribute(e, "modal-size", m::getModalSize, m::setModalSize);
        p.anyChildren(e, "regions", m::getRegions, m::setRegions, p.anyOf(N2oRegion.class), regionDefaultNamespace);
        p.children(e, null, "toolbar", m::getToolbars, m::setToolbars, new ToolbarIO());
        p.child(e, null, "search-bar", m::getSearchBar, m::setSearchBar, N2oSearchablePage.N2oSearchBar::new, this::searchBar);
        p.anyAttributes(e, m::getExtAttributes, m::setExtAttributes);
    }

    public void searchBar(Element e, N2oSearchablePage.N2oSearchBar sb, IOProcessor p) {
        p.attribute(e, "class", sb::getClassName, sb::setClassName);
        p.attributeEnum(e, "trigger", sb::getTrigger, sb::setTrigger, N2oSearchablePage.N2oSearchBar.TriggerType.class);
        p.attribute(e, "placeholder", sb::getPlaceholder, sb::setPlaceholder);
        p.attribute(e, "button-label", sb::getButtonLabel, sb::setButtonLabel);
        p.attribute(e, "button-icon", sb::getButtonIcon, sb::setButtonIcon);
        p.attribute(e, "search-widget-id", sb::getSearchWidgetId, sb::setSearchWidgetId);
        p.attribute(e, "search-filter-id", sb::getSearchFilterId, sb::setSearchFilterId);
        p.attribute(e, "search-param", sb::getSearchParam, sb::setSearchParam);
    }

    @Override
    public Class<N2oSearchablePage> getElementClass() {
        return N2oSearchablePage.class;
    }

    @Override
    public N2oSearchablePage newInstance(Element element) {
        return new N2oSearchablePage();
    }

    @Override
    public String getElementName() {
        return "searchable-page";
    }

    @Override
    public String getNamespaceUri() {
        return pageDefaultNamespace.getURI();
    }

    public void setRegionDefaultNamespace(String regionDefaultNamespace) {
        this.regionDefaultNamespace = Namespace.getNamespace(regionDefaultNamespace);
    }
}
