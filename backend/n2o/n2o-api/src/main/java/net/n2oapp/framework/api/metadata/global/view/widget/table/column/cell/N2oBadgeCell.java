package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oSwitch;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;
import net.n2oapp.framework.api.metadata.meta.badge.Position;

/**
 * Исходная модель ячейки со значком.
 */
@Getter
@Setter
@VisualComponent
public class N2oBadgeCell extends N2oAbstractCell {
    @VisualAttribute
    private Position position;
    @VisualAttribute
    private String text;
    @VisualAttribute
    private String textFormat;
    @VisualAttribute
    private String color;
    @VisualAttribute
    private String format;
    @VisualAttribute
    private ShapeType shape;
    @VisualAttribute
    private String imageFieldId;
    @VisualAttribute
    private Position imagePosition;
    @VisualAttribute
    private ShapeType imageShape;
    private N2oSwitch n2oSwitch;
}
