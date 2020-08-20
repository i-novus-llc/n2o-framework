package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.TriggerEnum;

/**
 * Ячейка с тултипом и раскрывающимся текстовым списком
 */
@Getter
@Setter
public class N2oTooltipListCell extends N2oAbstractCell {
    @JsonProperty
    private String label;
    @JsonProperty
    private String fewLabel;
    @JsonProperty
    private String manyLabel;
    @JsonProperty("labelDashed")
    private Boolean dashedLabel;
    @JsonProperty
    private TriggerEnum trigger;
}
