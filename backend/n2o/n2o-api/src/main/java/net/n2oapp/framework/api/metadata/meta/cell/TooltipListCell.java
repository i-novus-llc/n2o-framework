package net.n2oapp.framework.api.metadata.meta.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.TriggerEnum;

/**
 * Клиентская модель ячейки с тултипом и раскрывающимся текстовым списком
 */
@Getter
@Setter
public class TooltipListCell extends AbstractCell {
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
