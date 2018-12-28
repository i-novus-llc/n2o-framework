package net.n2oapp.framework.api.metadata.meta.action.copy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.meta.action.ActionPayload;

/**
 * Клиентская модель коомпонента copy
 */
@Getter
@Setter
public class CopyActionPayload implements ActionPayload {
    @JsonProperty
    private ClientModel source;
    @JsonProperty
    private ClientModel target;

    @Getter
    @Setter
    public static class ClientModel implements Compiled {
        @JsonProperty
        String prefix;
        @JsonProperty
        String key;

        public ClientModel(String key, String prefix) {
            this.prefix = prefix;
            this.key = key;
        }
    }
}
