package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.action.control.TargetEnum;

/**
 * Клиентская модель компонента вывода многострочного текста
 */
@Getter
@Setter
public class OutputList extends Control {
    @JsonProperty
    private String labelFieldId;
    @JsonProperty
    private String hrefFieldId;
    @JsonProperty
    private TargetEnum target;
    @JsonProperty
    private DirectionEnum direction;
    @JsonProperty
    private String separator;

    public enum DirectionEnum {
        row,
        column
    }
}
