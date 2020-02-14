package net.n2oapp.framework.api.metadata.global.view.page;

import lombok.Getter;
import lombok.Setter;

/**
 * Исходная модель страницы с поисковой строкой
 */
@Getter
@Setter
public class N2oSearchablePage extends N2oStandardPage {
    private N2oSearchBar searchBar;

    @Getter
    @Setter
    public static class N2oSearchBar {
        private String className;
        private String placeholder;
        private String buttonIcon;
        private String trigger;
        private String searchWidgetId;
        private String searchFilterId;
        private String searchParam;
    }
}
