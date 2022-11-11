package net.n2oapp.framework.api.metadata.meta.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.IconType;

/**
 * Клиентская модель ячейки со ссылкой
 */
@Getter
@Setter
public class LinkCell extends ActionCell {
    @JsonProperty
    private String icon;
    @JsonProperty
    private IconType type;
}
