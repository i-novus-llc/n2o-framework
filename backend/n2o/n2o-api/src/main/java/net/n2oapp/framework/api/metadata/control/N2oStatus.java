package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;
import net.n2oapp.framework.api.metadata.meta.badge.Position;

/**
 * Компонент отображения статуса
 */
@Getter
@Setter
@VisualComponent
public class N2oStatus extends N2oField {
    @VisualAttribute
    private String color;
    @VisualAttribute
    private String text;
    @VisualAttribute
    private Position textPosition;
}
