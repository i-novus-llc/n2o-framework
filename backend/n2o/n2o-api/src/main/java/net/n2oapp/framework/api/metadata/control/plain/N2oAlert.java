package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.Setter;

/**
 * Компонент вывода оповещения
 */
@Getter
@Setter
public class N2oAlert extends N2oPlainField {
    private String text;
    private String header;
    private String footer;
    private String color;
    private Boolean fade;
    private String tag;
}
