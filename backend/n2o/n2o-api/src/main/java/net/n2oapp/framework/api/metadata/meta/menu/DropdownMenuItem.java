package net.n2oapp.framework.api.metadata.meta.menu;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.TriggerEnum;
import net.n2oapp.framework.api.metadata.menu.N2oDropdownMenuItem;

import java.util.ArrayList;

/**
 * Клиентская модель элемента меню {@code <dropdown-menu>}
 */
@Getter
@Setter
public class DropdownMenuItem extends BaseMenuItem {
    @JsonProperty
    private TriggerEnum trigger;
    @JsonProperty
    private N2oDropdownMenuItem.PositionTypeEnum position;
    @JsonProperty
    private ArrayList<BaseMenuItem> content;
}