package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oSwitch;

/**
 * Ячейка со списком
 */
@Getter
@Setter
public class N2oListCell extends N2oAbstractCell {
    @JsonProperty
    private String labelFieldId;
    @JsonProperty
    private String color;
    @JsonProperty("amountToGroup")
    private Integer size;
    private N2oSwitch n2oSwitch;
}
