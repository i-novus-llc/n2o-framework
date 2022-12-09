package net.n2oapp.framework.api.metadata.meta.action.set_value;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.action.MergeMode;
import net.n2oapp.framework.api.metadata.meta.action.ActionPayload;

/**
 * Клиентская модель компонента set-value
 */
@Getter
@Setter
public class SetValueActionPayload implements ActionPayload {
    @JsonProperty
    private String sourceMapper;
    @JsonProperty
    private ClientModel source;
    @JsonProperty
    private ClientModel target;
    @JsonProperty
    private MergeMode mode;

    @Getter
    @Setter
    public static class ClientModel implements Compiled {
        @JsonProperty
        String prefix;
        @JsonProperty
        String key;
        @JsonProperty
        String field;

        public ClientModel(String key, String prefix) {
            this.prefix = prefix;
            this.key = key;
        }
    }


}
