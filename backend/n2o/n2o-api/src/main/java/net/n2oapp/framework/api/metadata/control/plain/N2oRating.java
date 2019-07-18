package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.Setter;

/**
 * Компонент ввода и отображения рейтинга
 */
@Getter
@Setter
public class N2oRating extends N2oPlainField {
    public N2oRating(String id) {
        setId(id);
    }

    public N2oRating() {

    }

    private Integer max;
    private Boolean half;
    private Boolean showToolTip;
}
