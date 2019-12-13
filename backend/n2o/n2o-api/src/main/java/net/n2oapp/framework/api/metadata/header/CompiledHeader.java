package net.n2oapp.framework.api.metadata.header;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Собранный хедер
 */
@Getter
@Setter
public class CompiledHeader extends Header {
    @JsonProperty
    private String brand;
    @JsonProperty
    private String brandImage;
    @JsonProperty
    private String activeId;
    @JsonProperty
    private String color;
    @JsonProperty
    private Boolean fixed;
    @JsonProperty
    private Boolean collapsed;
    @JsonProperty
    private String className;
    @JsonProperty
    private Map<String, String> style;
    @JsonProperty
    private Boolean search;
    private String welcomePage;
    @JsonProperty
    private SimpleMenu items;
    @JsonProperty
    private SimpleMenu extraItems;
    @JsonProperty
    private String src;
}
