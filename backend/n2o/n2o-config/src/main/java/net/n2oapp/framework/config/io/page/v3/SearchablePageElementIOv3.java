package net.n2oapp.framework.config.io.page.v3;

import net.n2oapp.framework.api.metadata.global.view.page.N2oSearchablePage;
import net.n2oapp.framework.api.metadata.global.view.region.N2oAbstractRegion;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.widget.WidgetIOv4;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись страницы с поисковой строкой версии 3.0
 */
@Component
public class SearchablePageElementIOv3 extends BasePageElementIOv3<N2oSearchablePage> {

    @Override
    public void io(Element e, N2oSearchablePage m, IOProcessor p) {
        super.io(e, m, p);
        p.child(e, null, "search-bar", m::getSearchBar, m::setSearchBar, N2oSearchablePage.N2oSearchBar::new, this::searchBar);
        p.anyChildren(e, "regions", m::getRegions, m::setRegions, p.anyOf(N2oAbstractRegion.class)
                .ignore(getWidgets()), getRegionDefaultNamespace());
        p.anyChildren(e, "regions", m::getWidgets, m::setWidgets, p.anyOf(N2oWidget.class)
                .ignore(getRegions()), WidgetIOv4.NAMESPACE);
    }

    public void searchBar(Element e, N2oSearchablePage.N2oSearchBar sb, IOProcessor p) {
        p.attribute(e, "class", sb::getClassName, sb::setClassName);
        p.attribute(e, "placeholder", sb::getPlaceholder, sb::setPlaceholder);
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
