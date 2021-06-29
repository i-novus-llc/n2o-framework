package net.n2oapp.framework.api.ui;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.exception.SeverityType;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Стандартное сообщение N2O
 */
@Getter
@Setter
public class ResponseMessage implements Serializable {
    @JsonProperty
    private String field;
    @JsonProperty("label")
    private String title;
    @JsonProperty
    private String severity;
    @JsonProperty
    private String text;
    @JsonProperty
    private Integer timeout;
    @JsonProperty
    private Boolean closeButton;
    @JsonProperty
    private String position = "relative";
    @JsonProperty
    private String placement;
    @JsonProperty
    private Boolean loader = false;
    @JsonProperty
    private Boolean animate = false;
    @JsonProperty
    private Map<String, String> choice;
    @JsonProperty
    private List<String> stacktrace;
    @JsonProperty
    private Object data;

    public void setSeverityType(SeverityType severity) {
        this.severity = severity != null ? severity.getId() : null;
    }
}
