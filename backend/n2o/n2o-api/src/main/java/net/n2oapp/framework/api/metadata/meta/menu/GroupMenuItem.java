package net.n2oapp.framework.api.metadata.meta.menu;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.menu.N2oGroupMenuItem;

import java.util.ArrayList;

/**
 * Клиентская модель элемента меню {@code <group>}
 */
@Getter
@Setter
public class GroupMenuItem extends BaseMenuItem {
    @JsonProperty
    private Boolean collapsible;
    @JsonProperty
    private N2oGroupMenuItem.GroupStateTypeEnum defaultState;
    @JsonProperty
    private ArrayList<BaseMenuItem> content;
}