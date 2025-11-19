package net.n2oapp.framework.api.metadata.meta.widget.toolbar;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель кнопки настройки таблицы resize
 */
@Getter
@Setter
public class ResizeButton extends PerformButton {
    @JsonProperty
    private Integer[] size;
}