package net.n2oapp.framework.api.metadata.control.filter_buttons;

import lombok.Getter;
import lombok.Setter;

/**
 * Компонент ClearButton (кнопка сброса фильтров)
 */
@Getter
@Setter
public class N2oClearButton extends N2oFilterButtonField {

    private String[] ignore;
}
