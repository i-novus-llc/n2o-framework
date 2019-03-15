package net.n2oapp.framework.api.metadata.meta.widget.toolbar;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Клиентская модель кнопки в меню
 */
@Getter
@Setter
public class Button extends MenuItem {
    @JsonProperty
    private List<MenuItem> subMenu;
    @JsonProperty
    private String dropdownSrc;
    @JsonProperty
    private String color;

}
