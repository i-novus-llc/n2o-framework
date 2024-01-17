package net.n2oapp.framework.api.metadata.control.filter_buttons;


import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.control.N2oField;

/**
 * Компонент SearchButtons (кнопки фильтра)
 */
@Getter
@Setter
public class N2oSearchButtons extends N2oField {
    private String searchLabel;
    private String resetLabel;
    private String[] clearIgnore;
}
