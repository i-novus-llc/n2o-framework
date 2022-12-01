package net.n2oapp.framework.api.metadata.meta.action.clear;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.ActionPayload;

/**
 * Клиентская модель clear-action
 */
@Getter
@Setter
public class ClearActionPayload implements ActionPayload {
    @JsonProperty
    private String[] prefixes;
    @JsonProperty
    private String key;
}
