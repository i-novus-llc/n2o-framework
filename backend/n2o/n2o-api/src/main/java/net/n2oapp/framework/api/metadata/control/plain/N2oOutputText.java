package net.n2oapp.framework.api.metadata.control.plain;


import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.IconType;
import net.n2oapp.framework.api.metadata.meta.badge.Position;

/**
 * Компонент вывода однострочного текста
 */
@Getter
@Setter
public class N2oOutputText extends N2oPlainField {
    private IconType type;
    private String icon;
    private Position iconPosition;
    private String format;
    private Boolean ellipsis;
    private String expandable;
}
