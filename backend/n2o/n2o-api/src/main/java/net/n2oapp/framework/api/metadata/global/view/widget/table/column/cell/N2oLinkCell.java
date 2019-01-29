package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.global.view.widget.table.IconType;


/**
 * Ячейка таблицы с ссылкой
 */
@Getter
@Setter
public class N2oLinkCell extends N2oActionCell {
    @JsonProperty
    private String id;
    private String style;
    @JsonProperty
    private String icon;
    @JsonProperty
    private IconType type;
    @JsonProperty
    private String url;
    @JsonProperty
    private Target target;
}
