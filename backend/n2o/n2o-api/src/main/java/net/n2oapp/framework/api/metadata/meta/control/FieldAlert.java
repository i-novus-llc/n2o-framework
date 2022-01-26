package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Клиентская модель поля для вывода оповещения
 */
@Getter
@Setter
public class FieldAlert extends Field {
    @JsonProperty
    private String text;
    @JsonProperty
    private  String title;
    @JsonProperty
    private String href;
    @JsonProperty
    private String header;
    @JsonProperty
    private String footer;
    @JsonProperty
    private String color;
    @JsonProperty
    private Boolean fade;
    @JsonProperty
    private String tag;
    @JsonProperty
    private Map<String, String> style;
}
