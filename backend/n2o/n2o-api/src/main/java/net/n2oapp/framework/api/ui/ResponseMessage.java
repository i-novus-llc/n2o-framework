package net.n2oapp.framework.api.ui;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.exception.SeverityType;

import java.io.Serializable;
import java.util.List;

/**
 * Стандартное сообщение N2O
 */
@Getter
@Setter
public class ResponseMessage implements Serializable {
    @JsonProperty
    private String field;
    @JsonProperty
    private String header;
    @JsonProperty
    private String color;
    @JsonProperty
    private String text;
    @JsonProperty
    private Style style;
    @JsonProperty
    private Integer timeout;
    @JsonProperty
    private Boolean closeButton;
    @JsonProperty
    private String href;
    @JsonProperty
    private Placement placement;
    @JsonProperty
    private List<String> stacktrace;
    @JsonProperty
    private Long timestamp;
    @JsonProperty
    private Object data;

    public void setSeverityType(SeverityType severity) {
        this.color = severity != null ? severity.getId() : null;
    }

}
