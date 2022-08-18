package net.n2oapp.framework.api.metadata.meta.widget;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель виджета дерево
 */
@Getter
@Setter
public class Tree extends Widget {
    @JsonProperty
    private String parentFieldId;
    @JsonProperty
    private String valueFieldId;
    @JsonProperty
    private String childrenFieldId;
    @JsonProperty
    private String labelFieldId;
    @JsonProperty
    private String iconFieldId;
    @JsonProperty
    private String imageFieldId;
    @JsonProperty
    private String badgeFieldId;
    @JsonProperty
    private String badgeColorFieldId;
    @JsonProperty
    private Boolean multiselect;
    @JsonProperty
    private Boolean hasCheckboxes;
    @JsonProperty
    private Boolean ajax;
    @JsonProperty
    private Boolean autoFocus = true;
    @JsonProperty
    private Boolean hasFocus = true;
    @JsonProperty
    private Boolean hasSelect = true;
}
