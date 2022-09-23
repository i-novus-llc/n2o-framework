package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oSwitch;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;

/**
 * Исходная модель ячейки со значком.
 */
@Getter
@Setter
public class N2oBadgeCell extends N2oAbstractCell {
    private Position position;
    private String text;
    private String textFormat;
    private String color;
    private String format;
    private ShapeType shape;
    private String imageFieldId;
    private Position imagePosition;
    private ShapeType imageShape;
    private N2oSwitch n2oSwitch;
}
