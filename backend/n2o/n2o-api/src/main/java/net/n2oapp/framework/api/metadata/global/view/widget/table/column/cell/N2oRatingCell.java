package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;

/**
 * Ячейка таблицы с рейтингом
 */
@Getter
@Setter
@VisualComponent
public class N2oRatingCell extends N2oActionCell {
    @VisualAttribute
    private Boolean showTooltip;
    @VisualAttribute
    private Boolean half;
    @VisualAttribute
    private Integer max;
    @VisualAttribute
    private Boolean readonly;
}
