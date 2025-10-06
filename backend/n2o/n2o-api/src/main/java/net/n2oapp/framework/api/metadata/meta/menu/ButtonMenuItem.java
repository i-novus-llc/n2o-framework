package net.n2oapp.framework.api.metadata.meta.menu;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.badge.Badge;

/**
 * Клиентская модель элемента меню {@code <button>}
 */
@Getter
@Setter
public class ButtonMenuItem extends BaseMenuItem {
    @JsonProperty
    private String color;
    @JsonProperty
    private String hint;
    @JsonProperty
    private String hintPosition;
    @JsonProperty
    private Badge badge;
    @JsonProperty
    private Action action;
}
