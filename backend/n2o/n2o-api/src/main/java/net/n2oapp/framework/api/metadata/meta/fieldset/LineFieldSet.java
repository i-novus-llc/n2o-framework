package net.n2oapp.framework.api.metadata.meta.fieldset;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель филдсета с горизонтальной линией
 */
@Getter
@Setter
public class LineFieldSet extends FieldSet {
    @JsonProperty
    private Boolean collapsible;
    @JsonProperty
    private Boolean expand;
    @JsonProperty
    private String label;
    @JsonProperty
    private final Boolean hasArrow = true;
    @JsonProperty
    private final String type = "line";
}
