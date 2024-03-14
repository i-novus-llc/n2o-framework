package net.n2oapp.framework.api.metadata.meta.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.badge.Position;

/**
 * Клиентская модель ячейки с иконкой
 */
@Getter
@Setter
public class IconCell extends AbstractCell {
    @JsonProperty
    private String icon;
    @JsonProperty
    private String text;
    @JsonProperty("iconPosition")
    private Position position;
}
