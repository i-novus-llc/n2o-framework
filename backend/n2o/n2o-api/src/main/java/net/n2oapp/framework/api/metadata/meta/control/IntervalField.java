package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Компонент ввода интервала
 */
@Getter
@Setter
public class IntervalField<C extends Control> extends Field {
    @JsonProperty
    protected C beginControl;

    @JsonProperty
    protected C endControl;
}