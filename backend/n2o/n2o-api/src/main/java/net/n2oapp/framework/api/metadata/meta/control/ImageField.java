package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель Image с заголовком и подзаголовком
 */
@Getter
@Setter
public class ImageField extends Field {
    @JsonProperty
    private String url;
    @JsonProperty
    private String data;
    @JsonProperty
    private String title;
    @JsonProperty
    private String description;
    @JsonProperty
    private Position textPosition;
    @JsonProperty
    private String width;

    public enum Position {
        top, left, right, bottom
    }
}
