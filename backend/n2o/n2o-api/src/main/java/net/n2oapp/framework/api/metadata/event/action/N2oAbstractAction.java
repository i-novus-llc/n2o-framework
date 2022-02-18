package net.n2oapp.framework.api.metadata.event.action;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;

import java.io.IOException;
import java.util.Map;

/**
 * Абстрактное действие
 */
@Getter
@Setter
public abstract class N2oAbstractAction implements N2oAction {
    @JsonProperty
    private String id;
    @JsonProperty
    private String src;
    @JsonProperty
    private String namespaceUri;
    @JsonProperty
    @JsonDeserialize(keyUsing = NameSpaceDeserializer.class)
    private Map<N2oNamespace, Map<String, String>> extAttributes;

    public static class NameSpaceDeserializer extends KeyDeserializer {
        @Override
        public N2oNamespace deserializeKey(String s, DeserializationContext deserializationContext) throws IOException {
            return null;
        }
    }
}
