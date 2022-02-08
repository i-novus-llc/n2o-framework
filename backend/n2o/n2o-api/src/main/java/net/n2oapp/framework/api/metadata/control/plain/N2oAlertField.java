package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.Setter;

/**
 * Компонент вывода уведомления
 */
@Getter
@Setter
public class N2oAlertField extends N2oPlainField {
    private String title;
    private String text;
    private String href;
    private String color;
    private Boolean closeButton;
}
