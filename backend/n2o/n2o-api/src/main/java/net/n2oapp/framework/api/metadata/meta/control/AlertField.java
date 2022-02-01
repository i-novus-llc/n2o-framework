package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель поля для вывода оповещения
 */
@Getter
@Setter
public class AlertField extends Field {
    @JsonProperty
    private String text;
    @JsonProperty
    private  String title;
    @JsonProperty
    private String href;
    @JsonProperty
    private String color;
    @JsonProperty
    private Boolean closeButton;
}
