package net.n2oapp.framework.api.metadata.meta.widget.toolbar;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Абстрактная модель кнопки в меню
 */
@Getter
@Setter
public abstract class AbstractButton extends MenuItem {

    @JsonProperty
    private String color;
}
