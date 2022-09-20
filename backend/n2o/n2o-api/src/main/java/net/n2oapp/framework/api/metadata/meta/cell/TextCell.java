package net.n2oapp.framework.api.metadata.meta.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель ячейки с текстом
 */
@Getter
@Setter
public class TextCell extends AbstractCell {
    @JsonProperty
    private String format;
    @JsonProperty
    private String subTextFieldKey;
    @JsonProperty
    private String subTextFormat;
}
