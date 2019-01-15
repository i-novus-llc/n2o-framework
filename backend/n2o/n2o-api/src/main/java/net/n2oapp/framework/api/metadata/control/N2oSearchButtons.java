package net.n2oapp.framework.api.metadata.control;


import lombok.Getter;
import lombok.Setter;

/**
 * Компонент SearchButtons (кнопки фильтра)
 */
@Getter
@Setter
public class N2oSearchButtons extends N2oField {

    private String searchLabel;
    private String resetLabel;
}
