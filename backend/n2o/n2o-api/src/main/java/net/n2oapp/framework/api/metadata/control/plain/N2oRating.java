package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;

/**
 * Компонент ввода и отображения рейтинга
 */
@Getter
@Setter
@VisualComponent
public class N2oRating extends N2oPlainField {
    @VisualAttribute
    private Integer max;
    @VisualAttribute
    private Boolean half;
    @VisualAttribute
    private Boolean showTooltip;
}
