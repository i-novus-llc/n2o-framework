package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ImageShape;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.ImageStatusElement;
import net.n2oapp.framework.api.metadata.meta.control.TextPosition;

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
    private TextPosition textPosition;
    private ImageShape shape;
    private String width;
    private ImageStatusElement[] statuses;
}
