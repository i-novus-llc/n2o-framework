package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.Setter;

/**
 * Компонент вывода оповещения
 */
@Getter
@Setter
public class N2oAlert extends N2oPlainField {
    private String header;
    private String text;
    private String style;
    private String cssClass;
    private String href;
    private String color;
    private Boolean closeButton;
}
