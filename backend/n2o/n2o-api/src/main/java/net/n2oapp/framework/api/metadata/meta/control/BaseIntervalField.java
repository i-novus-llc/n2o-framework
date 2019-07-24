package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseIntervalField<C extends Control> extends Field {
    @JsonProperty("beginControl")
    protected C beginControl;

    @JsonProperty("endControl")
    protected C endControl;

}




