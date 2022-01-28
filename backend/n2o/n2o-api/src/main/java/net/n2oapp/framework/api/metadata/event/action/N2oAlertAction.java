package net.n2oapp.framework.api.metadata.event.action;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePlacement;

/**
 * Действие оповещения
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
    private Boolean closeButton;
    private Integer timeout;
    private MessagePlacement placement;
}
