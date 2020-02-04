package net.n2oapp.framework.api.metadata.global.view.page;

import lombok.Getter;
import lombok.Setter;

/**
 * Исходная модель страницы с полем поиска
 */
@Getter
@Setter
public class N2oSearchablePage extends N2oStandardPage {
    private N2oSearchBar searchBar;

    @Getter
    @Setter
    public static class N2oSearchBar {
        private String placeholder;
        private String buttonIcon;
        private String searchWidgetId;
        private String searchFilterId;
        private String searchParam;
    }
}
