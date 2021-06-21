package net.n2oapp.framework.api.metadata.application;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;

import java.util.Map;

/**
 * Клиентская модель подвала сайта
 */
@Getter
@Setter
public class Footer implements Compiled {
    @JsonProperty
    private String src;
    @JsonProperty
    private String className;
    @JsonProperty
    private Map<String, String> style;
    @JsonProperty
    private String textRight;
    @JsonProperty
    private String textLeft;
}
