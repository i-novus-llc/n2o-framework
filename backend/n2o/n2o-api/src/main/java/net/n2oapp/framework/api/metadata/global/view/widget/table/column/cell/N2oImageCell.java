package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;

/**
 * Ячейка с изображением
 */
@Getter
@Setter
public class N2oImageCell extends N2oActionCell {
    private String width;
    private ShapeType shape;
    private String title;
    private String data;
    private String description;
    private Position textPosition;
    private ImageStatusElement[] statuses;

    public enum Position {
        top, left, right, bottom
    }
}
