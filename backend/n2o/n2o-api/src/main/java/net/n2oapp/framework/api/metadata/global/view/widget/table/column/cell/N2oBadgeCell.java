package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oSwitch;

/**
 * Исходная модель ячейки с текстом.
 */
@Getter
@Setter
public class N2oBadgeCell extends N2oAbstractCell {
    @JsonProperty("placement")
    private Position position;
    @JsonProperty
    private String text;
    @JsonProperty("format")
    private String textFormat;
    @JsonProperty
    private String color;
    @JsonProperty("badgeFormat")
    private String format;
    private N2oSwitch n2oSwitch;
}
