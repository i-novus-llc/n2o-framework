package net.n2oapp.framework.api.metadata.control.plain;


import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;
import net.n2oapp.framework.api.metadata.global.view.widget.table.IconType;
import net.n2oapp.framework.api.metadata.meta.badge.Position;

/**
 * Компонент вывода однострочного текста
 */
@Getter
@Setter
@VisualComponent
public class N2oOutputText extends N2oPlainField {
    @VisualAttribute
    private IconType type;
    @VisualAttribute
    private String icon;
    @VisualAttribute
    private Position iconPosition;
    @VisualAttribute
    private String format;
    @VisualAttribute
    private Boolean ellipsis;
    @VisualAttribute
    private String expandable;
}
