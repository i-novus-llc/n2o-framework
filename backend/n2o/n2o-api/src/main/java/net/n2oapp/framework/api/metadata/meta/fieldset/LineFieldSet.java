package net.n2oapp.framework.api.metadata.meta.fieldset;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.badge.Badge;

/**
 * Клиентская модель филдсета с горизонтальной линией
 */
@Getter
@Setter
public class LineFieldSet extends FieldSet {
    @JsonProperty
    private Boolean collapsible;
    @JsonProperty
    private Boolean hasSeparator;
    @JsonProperty
    private Boolean expand;
    @JsonProperty
    private final String type = "line";
    @JsonProperty
    private Badge badge;
}
