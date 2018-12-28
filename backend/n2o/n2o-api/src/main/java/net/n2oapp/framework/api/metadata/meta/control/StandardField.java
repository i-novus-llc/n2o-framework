package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


/**
 * Клиентская молель стандартного поля
 */
@Getter
@Setter
public class StandardField<T extends Control> extends Field {
    @JsonProperty
    private String label;
    @JsonProperty
    private String labelClass;
    @JsonProperty
    private String description;
    private Integer colSize;
    @JsonProperty("control")
    protected T control;
    @JsonProperty
    private String help;
}
