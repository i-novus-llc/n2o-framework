package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;

/**
 * Компонент отображения прогресса
 */
@Getter
@Setter
@VisualComponent
public class N2oProgress extends N2oPlainField {
    @VisualAttribute
    private Integer max;
    @VisualAttribute
    private String barText;
    @VisualAttribute
    private Boolean animated;
    @VisualAttribute
    private Boolean striped;
    @VisualAttribute
    private String color;
    @VisualAttribute
    private String barClass;
}
