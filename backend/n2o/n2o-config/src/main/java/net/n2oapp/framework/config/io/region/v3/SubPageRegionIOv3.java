package net.n2oapp.framework.config.io.region.v3;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.view.ActionBar;
import net.n2oapp.framework.api.metadata.global.view.page.N2oBreadcrumb;
import net.n2oapp.framework.api.metadata.global.view.region.N2oSubPageRegion;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.config.io.action.v2.ActionIOv2;
import net.n2oapp.framework.config.io.datasource.DatasourceIOv1;
import net.n2oapp.framework.config.io.toolbar.v2.ToolbarIOv2;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись региона `<sub-page>` версии 3.0
 */
@Component
public class SubPageRegionIOv3 implements NamespaceIO<N2oSubPageRegion> {

    @Override
    public void io(Element e, N2oSubPageRegion r, IOProcessor p) {
        p.attribute(e, "default-page-id", r::getDefaultPageId, r::setDefaultPageId);
        p.children(e, null, "page", r::getPages, r::setPages, N2oSubPageRegion.Page.class, this::page);
    }

    private void page(Element e, N2oSubPageRegion.Page page, IOProcessor p) {
        p.attribute(e, "page-id", page::getPageId, page::setPageId);
        p.attribute(e, "route", page::getRoute, page::setRoute);
        p.children(e, "breadcrumbs", "crumb", page::getBreadcrumbs, page::setBreadcrumbs,
                N2oBreadcrumb.class, this::breadcrumbs);
        p.anyChildren(e, "datasources", page::getDatasources, page::setDatasources,
                p.anyOf(N2oAbstractDatasource.class), DatasourceIOv1.NAMESPACE);
        p.children(e, "toolbars", "toolbar", page::getToolbars, page::setToolbars, new ToolbarIOv2());
        p.children(e, "actions", "action", page::getActions, page::setActions, ActionBar::new, this::action);
    }

    private void breadcrumbs(Element e, N2oBreadcrumb c, IOProcessor p) {
        p.attribute(e, "label", c::getLabel, c::setLabel);
        p.attribute(e, "path", c::getPath, c::setPath);
    }

    private void action(Element e, ActionBar a, IOProcessor p) {
        p.attribute(e, "id", a::getId, a::setId);
        p.anyChildren(e, null, a::getN2oActions, a::setN2oActions, p.anyOf(N2oAction.class), ActionIOv2.NAMESPACE);
    }

    @Override
    public Class<N2oSubPageRegion> getElementClass() {
        return N2oSubPageRegion.class;
    }

    @Override
    public String getElementName() {
        return "sub-page";
    }

    @Override
    public String getNamespaceUri() {
        return RegionIOv3.NAMESPACE.getURI();
    }
}
