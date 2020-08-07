package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;

/**
 * Клиентская модель компонента вывода многострочного текста
 */
@Getter
@Setter
public class OutputList extends Control {
    @JsonProperty
    private String labelFieldId;
    @JsonProperty
    private String linkFieldId;
    @JsonProperty
    private Target target;
    @JsonProperty
    private Direction direction;
    @JsonProperty
    private String separator;

    public enum Direction {
        row,
        column
    }
}
