package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.Setter;

/**
 * Компонент ввода и отображения рейтинга
 */
@Getter
@Setter
public class N2oRating extends N2oPlainField {
    private Integer max;
    private Boolean half;
    private Boolean showTooltip;
}
