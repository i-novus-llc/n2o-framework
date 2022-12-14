package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.badge.Position;

/**
 * Компонент отображения статуса
 */
@Getter
@Setter
public class N2oStatus extends N2oField {
    private String color;
    private String text;
    private Position textPosition;
}
