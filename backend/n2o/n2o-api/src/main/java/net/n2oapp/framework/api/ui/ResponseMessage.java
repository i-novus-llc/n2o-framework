package net.n2oapp.framework.api.ui;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.exception.SeverityTypeEnum;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePlacementEnum;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Стандартное сообщение N2O
 */
@Getter
@Setter
public class ResponseMessage implements Compiled {
    @JsonProperty
    private String id;
    @JsonProperty
    private String field;
    @JsonProperty
    private String title;
    @JsonProperty
    private String severity;
    @JsonProperty
    private String className;
    @JsonProperty
    private String text;
    @JsonProperty
    private Integer timeout;
    @JsonProperty
    private Boolean closeButton = true;
    @JsonProperty
    private String href;
    @JsonProperty
    private String modelLink;
    @JsonProperty
    private MessagePlacementEnum placement;
    @JsonProperty
    private Map<String, String> style;
    @JsonProperty("stacktrace")
    private List<String> payload;
    @JsonProperty
    private LocalDateTime time;

    @JsonProperty
    public String getId() {
        return UUID.randomUUID().toString();
    }

    public void setSeverityType(SeverityTypeEnum severity) {
        this.severity = severity != null ? severity.getId() : null;
    }

}
