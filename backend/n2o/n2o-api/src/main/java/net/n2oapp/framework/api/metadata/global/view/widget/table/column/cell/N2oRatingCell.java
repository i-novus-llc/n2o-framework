package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Ячейка таблицы с рейтингом
 */
@Getter
@Setter
public class N2oRatingCell extends N2oAbstractCell {
    @JsonProperty
    private Boolean showTooltip;
    @JsonProperty
    private Boolean half;
    @JsonProperty
    private Integer max;
}
