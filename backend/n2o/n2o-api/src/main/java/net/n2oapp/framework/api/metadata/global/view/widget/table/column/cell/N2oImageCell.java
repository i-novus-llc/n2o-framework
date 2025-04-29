package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeTypeEnum;

/**
 * Ячейка с изображением
 */
@Getter
@Setter
public class N2oImageCell extends N2oActionCell {
    private String width;
    private ShapeTypeEnum shape;
    private String title;
    private String data;
    private String description;
    private PositionEnum textPosition;
    private N2oImageStatusElement[] statuses;

    public enum PositionEnum {
        top, left, right, bottom
    }
}
