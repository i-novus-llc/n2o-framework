package net.n2oapp.framework.api.metadata.menu;

import lombok.Getter;
import lombok.Setter;

/**
 * Исходная модель элемента меню {@code <button>}
 */
@Getter
@Setter
public class N2oButtonMenuItem extends N2oMenuItem {
    private String color;
    private String description;
    private String tooltipPosition;
}
