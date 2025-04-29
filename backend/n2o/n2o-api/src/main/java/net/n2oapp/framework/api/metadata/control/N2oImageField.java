package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeTypeEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oImageStatusElement;
import net.n2oapp.framework.api.metadata.meta.control.TextPositionEnum;

/**
 * Исходная модель компонента вывода изображения
 */
@Getter
@Setter
public class N2oImageField extends N2oActionField {
    private String url;
    private String data;
    private String title;
    private String description;
    private TextPositionEnum textPosition;
    private ShapeTypeEnum shape;
    private String width;
    private N2oImageStatusElement[] statuses;
}
