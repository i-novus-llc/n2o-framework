package net.n2oapp.framework.api.metadata.event.action;


import lombok.Getter;
import lombok.Setter;

/**
 * Действие открытия drawer окна
 */
@Getter
@Setter
public class N2oOpenDrawer extends N2oAbstractPageAction {
    private Boolean closable;
    private Boolean backdrop;
//    private String width; //N2oAbstractPageAction.width
    private String height;
    private String placement;
    private String level;
    private Boolean backdropClosable;
    private Boolean fixedFooter;
}
