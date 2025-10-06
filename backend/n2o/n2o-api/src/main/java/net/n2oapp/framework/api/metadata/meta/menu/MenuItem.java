package net.n2oapp.framework.api.metadata.meta.menu;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.badge.Badge;

/**
 * Клиентская модель элемента меню {@code <menu-item>}
 */
@Getter
@Setter
public class MenuItem extends BaseMenuItem {
    @JsonProperty
    private Badge badge;
    @JsonProperty
    private Action action;
}
