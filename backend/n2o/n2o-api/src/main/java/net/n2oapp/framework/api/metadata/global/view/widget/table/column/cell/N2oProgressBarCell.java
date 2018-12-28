package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oSwitch;
import net.n2oapp.framework.api.metadata.global.view.widget.table.Size;

/**
 * Ячейка с ProgressBar
 */
@Getter
@Setter
public class N2oProgressBarCell extends N2oAbstractCell {
    private N2oSwitch styleSwitch;
    @JsonProperty
    private Size size;
    @JsonProperty
    private Boolean striped;
    @JsonProperty("animated")
    private Boolean active;
    private String value;
    @JsonProperty
    private String color;

}
