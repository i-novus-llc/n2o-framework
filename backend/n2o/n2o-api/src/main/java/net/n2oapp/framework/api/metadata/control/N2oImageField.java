package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oImageStatusElement;
import net.n2oapp.framework.api.metadata.meta.control.TextPosition;

/**
 * Исходная модель компонента вывода изображения
 */
@Getter
@Setter
@VisualComponent
public class N2oImageField extends N2oActionField {
    @VisualAttribute
    private String url;
    @VisualAttribute
    private String data;
    @VisualAttribute
    private String title;
    @VisualAttribute
    private String description;
    @VisualAttribute
    private TextPosition textPosition;
    @VisualAttribute
    private ShapeType shape;
    @VisualAttribute
    private String width;
    @VisualAttribute
    private N2oImageStatusElement[] statuses;
}
