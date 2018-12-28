package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.IconType;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.Position;


/**
 * Клиентская модель поля для вывода текста
 */
@Getter
@Setter
public class OutputText extends Control {
    @JsonProperty
    private IconType type;
    @JsonProperty
    private String className;
    @JsonProperty
    private String icon;
    @JsonProperty
    private String format;
    @JsonProperty("textPlace")
    private Position position;
}
