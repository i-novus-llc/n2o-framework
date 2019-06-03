package net.n2oapp.framework.api.metadata.meta.action.close;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.ActionPayload;

@Getter
@Setter
public class CloseActionPayload implements ActionPayload {
    @JsonProperty("name")
    private String pageId;
    @JsonProperty
    private Boolean prompt;
}
