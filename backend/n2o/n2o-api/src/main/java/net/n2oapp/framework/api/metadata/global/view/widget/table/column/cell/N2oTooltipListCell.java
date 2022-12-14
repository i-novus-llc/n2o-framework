package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.TriggerEnum;

/**
 * Ячейка с тултипом и раскрывающимся текстовым списком
 */
@Getter
@Setter
public class N2oTooltipListCell extends N2oAbstractCell {
    private String label;
    private String fewLabel;
    private String manyLabel;
    private Boolean dashedLabel;
    private TriggerEnum trigger;
}
