package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.control.plain.CodeLanguageEnum;

/**
 * Клиентская модель редактора кода
 */
@Getter
@Setter
public class CodeEditor extends Control {
    @JsonProperty
    private String name;
    @JsonProperty
    private CodeLanguageEnum lang;
    @JsonProperty
    private Boolean autocomplete;
    @JsonProperty
    private Integer minLines;
    @JsonProperty
    private Integer maxLines;
}
