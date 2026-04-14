package net.n2oapp.framework.api.metadata.meta.control;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.TriggerEnum;

/**
 * Клиентская модель чекбокса
 */
@Getter
@Setter
public class Checkbox extends Control {
    @JsonProperty
    private Boolean readOnly;
    @JsonProperty
    private String defaultUnchecked;
    @JsonProperty
    private String help;
    @JsonProperty
    private TriggerEnum helpTrigger;
}
