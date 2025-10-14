package net.n2oapp.framework.api.metadata.meta.widget.toolbar;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ClipboardButtonDataEnum;

/**
 * Клиентская модель кнопки копирования в буфер обмена
 */
@Getter
@Setter
public class ClipboardButton extends AbstractButton {
    @JsonProperty
    private String data;
    @JsonProperty
    private ClipboardButtonDataEnum type;
    @JsonProperty
    private String message;
}