package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.TriggerEnum;

/**
 * Ячейка с тултипом и раскрывающимся текстовым списком
 */
@Getter
@Setter
@VisualComponent
public class N2oTooltipListCell extends N2oAbstractCell {
    @VisualAttribute
    private String label;
    @VisualAttribute
    private String fewLabel;
    @VisualAttribute
    private String manyLabel;
    @VisualAttribute
    private Boolean dashedLabel;
    @VisualAttribute
    private TriggerEnum trigger;
}
