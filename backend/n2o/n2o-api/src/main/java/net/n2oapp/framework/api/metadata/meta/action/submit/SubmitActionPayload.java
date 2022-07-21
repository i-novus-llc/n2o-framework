package net.n2oapp.framework.api.metadata.meta.action.submit;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.ActionPayload;

@Getter
@Setter
public class SubmitActionPayload implements ActionPayload {
    @JsonProperty
    private String datasource;
}
