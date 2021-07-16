package net.n2oapp.framework.api.metadata.application;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;

/**
 * Клиентская модель логотипа и названия
 */
@Getter
@Setter
public class Logo implements Compiled {
    @JsonProperty
    private String title;
    @JsonProperty
    private String className;
    @JsonProperty
    private String style;
    @JsonProperty
    private String href;
    @JsonProperty
    private String src;
}
