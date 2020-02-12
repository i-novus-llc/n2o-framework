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
    private String color;
    private N2oSimpleMenu menu;
    private N2oSimpleMenu extraMenu;
    private String homePageUrl;
    private String welcomePageId;
    private String projectName;
    private String projectImageSrc;
    private String profilePageId;
    private String userMenuSrc;
    private String userContext;
    private String loginUrl;
    private String logoutUrl;
    private String registrationUrl;
}