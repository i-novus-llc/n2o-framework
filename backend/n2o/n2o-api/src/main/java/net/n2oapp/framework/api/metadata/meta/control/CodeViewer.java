package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.control.plain.CodeLanguageEnum;

/**
 * Клиентская модель компонента для просмотра кода
 */
@Getter
@Setter
public class CodeViewer extends Control {
    @JsonProperty("value")
    private String text;
    @JsonProperty
    private CodeLanguageEnum language;
    @JsonProperty
    private Boolean darkTheme;
    @JsonProperty
    private Boolean showLineNumbers;
    @JsonProperty
    private Integer startingLineNumber;
    @JsonProperty
    private Boolean hideButtons;
    @JsonProperty
    private Boolean hideOverflow;
}
