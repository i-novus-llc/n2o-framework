package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Select extends ListControl {
    @JsonProperty
    private Boolean hasCheckboxes;
}
