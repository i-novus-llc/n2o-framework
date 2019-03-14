package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель редактора текста
 */
@Getter
@Setter
public class TextEditor extends InputText {
    @JsonProperty
    private String name;
    @JsonProperty
    private Object toolbarConfig;
}
