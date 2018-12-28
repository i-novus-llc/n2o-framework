package net.n2oapp.framework.api.metadata.meta;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Свойства страницы
 */
@Getter
@Setter
public class PageProperty implements Serializable {
    @JsonProperty
    private String title;
    @JsonProperty
    private String modelLink;
}
