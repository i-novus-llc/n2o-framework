package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;
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

    @RequiredArgsConstructor
    @Getter
    public enum PositionEnum implements N2oEnum {
        TOP("top"),
        LEFT("left"),
        RIGHT("right"),
        BOTTOM("bottom");

        private final String id;
    }
}
