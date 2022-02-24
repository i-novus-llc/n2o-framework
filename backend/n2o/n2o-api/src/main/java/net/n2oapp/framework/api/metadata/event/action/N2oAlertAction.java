package net.n2oapp.framework.api.metadata.event.action;

import lombok.Getter;
import lombok.Setter;

/**
 * Действие уведомления
 */
@Getter
@Setter
public class N2oAlertAction extends N2oAbstractAction {

    private String title;
    private String text;
    private String href;
    private String color;
    private String style;
    private String cssClass;
    private String time;
    private String timeout;
    private String placement;
    private Boolean closeButton;

}
