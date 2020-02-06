package net.n2oapp.framework.config.io.page;

import net.n2oapp.framework.api.metadata.global.view.page.N2oSearchablePage;
import net.n2oapp.framework.api.metadata.global.view.region.N2oRegion;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись страницы с поисковой строкой
 */
@Component
public class SearchablePageElementIOv2 extends BasePageElementIOv2<N2oSearchablePage> {

    @Override
    public void io(Element e, N2oSearchablePage m, IOProcessor p) {
        super.io(e, m, p);
        p.anyChildren(e, "regions", m::getRegions, m::setRegions, p.anyOf(N2oRegion.class), getRegionDefaultNamespace());
        p.child(e, null, "search-bar", m::getSearchBar, m::setSearchBar, N2oSearchablePage.N2oSearchBar::new, this::searchBar);
    }

    public void searchBar(Element e, N2oSearchablePage.N2oSearchBar sb, IOProcessor p) {
        p.attribute(e, "placeholder", sb::getPlaceholder, sb::setPlaceholder);
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
}
