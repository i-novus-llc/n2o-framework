package net.n2oapp.framework.api.metadata.action;


import lombok.Getter;
import lombok.Setter;

/**
 * Действие открытия drawer окна
 */
@Getter
@Setter
public class N2oOpenDrawer extends N2oAbstractPageAction {
    private String height;
    private String placement;
    private Boolean closable;
    private Boolean backdrop;
    private Boolean closeOnEscape;
    private Boolean closeOnBackdrop;
    private Boolean fixedFooter;
}
