package net.n2oapp.framework.api.metadata.meta.fieldset;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель филдсета с динамическим числом полей
 */
@Getter
@Setter
public class MultiFieldSet extends FieldSet {
    @JsonProperty
    private String name;
    @JsonProperty
    private String childrenLabel;
    @JsonProperty
    private String firstChildrenLabel;
    @JsonProperty
    private String addButtonLabel;
    @JsonProperty
    private String removeAllButtonLabel;
    @JsonProperty
    private Object needAddButton;
    @JsonProperty
    private Object needCopyButton;
    @JsonProperty
    private Object needRemoveButton;
    @JsonProperty
    private Object needRemoveAllButton;
    @JsonProperty
    private Object canRemoveFirstItem;
    @JsonProperty
    private String primaryKey;
    @JsonProperty
    private Boolean generatePrimaryKey;
}
