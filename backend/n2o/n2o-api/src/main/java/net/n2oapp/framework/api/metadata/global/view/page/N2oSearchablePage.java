package net.n2oapp.framework.api.metadata.global.view.page;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Source;

/**
 * Исходная модель страницы с поисковой строкой
 */
@Getter
@Setter
public class N2oSearchablePage extends N2oStandardPage {
    private N2oSearchBar searchBar;

    @Getter
    @Setter
    public static class N2oSearchBar implements Source {
        private String className;
        private String placeholder;
        private String datasource;
        @Deprecated
        private String searchWidgetId;
        private String searchFilterId;
        private String searchParam;
    }
}
