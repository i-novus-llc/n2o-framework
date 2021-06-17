package net.n2oapp.framework.api.metadata.header;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.menu.N2oSimpleMenu;

/**
 * Простой хедер
 */
@Getter
@Setter
public class N2oSimpleHeader extends N2oHeader {
    private String src;
    private String cssClass;
    private String style;
    private boolean visible;
    private N2oSimpleMenu menu;
    private N2oSimpleMenu extraMenu;
    private String homePageUrl;
    private String welcomePageId;
    private String title;
    private String logoSrc;
    private String sidebarDefaultIcon;
    private String sidebarToggledIcon;
    private N2oSearchBar searchBar;
}