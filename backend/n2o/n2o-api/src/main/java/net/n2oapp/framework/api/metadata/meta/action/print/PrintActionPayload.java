package net.n2oapp.framework.api.metadata.meta.action.print;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.PrintType;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.ActionPayload;

import java.util.Map;

/**
 * Клиентская модель компонента print
 */
@Getter
@Setter
public class PrintActionPayload implements ActionPayload {
    @JsonProperty
    private String url;
    @JsonProperty
    private Map<String, ModelLink> pathMapping;
    @JsonProperty
    private Map<String, ModelLink> queryMapping;
    @JsonProperty
    private PrintType type;
    @JsonProperty
    private Boolean keepIndent;
    @JsonProperty
    private String documentTitle;
    @JsonProperty
    private Boolean loader;
    @JsonProperty
    private String loaderText;
    @JsonProperty
    private Boolean base64;
}
