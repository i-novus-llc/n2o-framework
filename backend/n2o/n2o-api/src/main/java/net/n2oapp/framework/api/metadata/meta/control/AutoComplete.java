package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.control.plain.MaskPasteModeEnum;


/**
 * Клиентская модель компонента ввода текста с автоподбором
 */
@Getter
@Setter
public class AutoComplete extends ListControl {
    @JsonProperty
    private Boolean tags;
    @JsonProperty
    private Integer maxTagTextLength;
    @JsonProperty
    private String inputLabelFieldId;
    @JsonProperty
    private String mask;
    @JsonProperty
    private MaskPasteModeEnum maskPasteMode;
}
