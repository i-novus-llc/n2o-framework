package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Ячейка таблицы с раскрывающимся тултипом с текстовым списком
 */
@Getter
@Setter
public class N2oTooltipListCell extends N2oAbstractCell {
    @JsonProperty
    private String labelFieldId;
    @JsonProperty
    private TriggerEnum trigger;

    /**
     * Тип действия, при котором раскрывается список
     */
    public enum TriggerEnum {
        hover,
        click
    }
}
