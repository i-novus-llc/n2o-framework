package net.n2oapp.framework.api.metadata.global.view.region;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.view.ActionBar;
import net.n2oapp.framework.api.metadata.global.view.page.N2oBreadcrumb;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;

/**
 * Исходная модель `<sub-page>`
 */
@Getter
@Setter
public class N2oSubPageRegion extends N2oRegion {
    private String defaultPageId;
    private Page[] pages;

    @Getter
    @Setter
    public static class Page implements Source {
        private String pageId;
        private String route;

        private N2oAbstractDatasource[] datasources;
        private N2oBreadcrumb[] breadcrumbs;
        private N2oToolbar[] toolbars;
        private ActionBar[] actions;
    }
}
