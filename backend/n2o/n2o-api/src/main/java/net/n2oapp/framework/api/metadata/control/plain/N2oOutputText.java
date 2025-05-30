package net.n2oapp.framework.api.metadata.control.plain;


import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.badge.PositionEnum;

/**
 * Компонент вывода однострочного текста
 */
@Getter
@Setter
public class N2oOutputText extends N2oPlainField {
    private String icon;
    private PositionEnum iconPosition;
    private String format;
}
