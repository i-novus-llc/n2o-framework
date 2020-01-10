package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.Setter;

/**
 * Компонент ввода пароля
 */
@Getter
@Setter
public class N2oPassword extends N2oPlainField {
    private Integer length;
    private Boolean eye;
}
