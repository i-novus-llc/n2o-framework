package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.action.LabelType;
import net.n2oapp.framework.api.metadata.global.view.widget.table.IconType;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oSwitch;

/**
 * Ячейка с иконкой
 */
@Getter
@Setter
public class N2oIconCell extends N2oAbstractCell {
    private N2oSwitch iconSwitch;
    private LabelType type;
    @JsonProperty("type")
    private IconType iconType;
    @JsonProperty
    private String icon;
    @JsonProperty
    private String text;
    @JsonProperty("textPlace")
    private Position position;
}
