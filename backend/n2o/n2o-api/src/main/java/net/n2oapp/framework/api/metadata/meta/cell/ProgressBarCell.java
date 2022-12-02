package net.n2oapp.framework.api.metadata.meta.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oProgressBarCell;

/**
 * Клиентская модель ячейки с индикатором прогресса
 */
@Getter
@Setter
public class ProgressBarCell extends AbstractCell {
    @JsonProperty
    private N2oProgressBarCell.Size size;
    @JsonProperty
    private Boolean striped;
    @JsonProperty("animated")
    private Boolean active;
    @JsonProperty
    private String color;
}
