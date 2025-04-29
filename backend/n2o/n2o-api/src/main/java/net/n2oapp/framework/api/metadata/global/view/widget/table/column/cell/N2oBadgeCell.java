package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oSwitch;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeTypeEnum;
import net.n2oapp.framework.api.metadata.meta.badge.PositionEnum;

/**
 * Исходная модель ячейки со значком.
 */
@Getter
@Setter
public class N2oBadgeCell extends N2oAbstractCell {
    private PositionEnum position;
    private String text;
    private String textFormat;
    private String color;
    private String format;
    private ShapeTypeEnum shape;
    private String imageFieldId;
    private PositionEnum imagePosition;
    private ShapeTypeEnum imageShape;
    private N2oSwitch n2oSwitch;
}
