package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import lombok.Getter;
import lombok.Setter;

/**
 * Ячейка таблицы с рейтингом
 */
@Getter
@Setter
public class N2oRatingCell extends N2oActionCell {
    private Boolean showTooltip;
    private Boolean half;
    private Integer max;
    private Boolean readonly;
}
