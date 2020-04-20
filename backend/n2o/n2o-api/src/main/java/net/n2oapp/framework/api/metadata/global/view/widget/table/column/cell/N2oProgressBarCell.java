package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Ячейка с индикатором прогресса
 */
@Getter
@Setter
public class N2oProgressBarCell extends N2oAbstractCell {
    @JsonProperty
    private Size size;
    @JsonProperty
    private Boolean striped;
    @JsonProperty("animated")
    private Boolean active;
    @JsonProperty
    private String color;

    public enum Size {
        small, normal, large
    }
}
