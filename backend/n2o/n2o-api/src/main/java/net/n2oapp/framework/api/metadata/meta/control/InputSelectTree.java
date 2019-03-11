package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.control.list.CheckingStrategy;

@Getter
@Setter
public class InputSelectTree extends ListControl {
    @JsonProperty
    private String placeholder;
    @JsonProperty
    private String parentFieldId;
    @JsonProperty
    private String hasChildrenFieldId;
    @JsonProperty
    private Boolean resetOnBlur;
    @JsonProperty
    private Boolean multiSelect;
    @JsonProperty
    private boolean hasCheckboxes;
    @JsonProperty
    private boolean ajax;
    @JsonProperty("showCheckedStrategy")
    private CheckingStrategy checkingStrategy;
    @JsonProperty
    private Integer maxTagCount;
}
