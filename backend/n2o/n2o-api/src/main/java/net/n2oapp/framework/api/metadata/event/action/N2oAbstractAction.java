package net.n2oapp.framework.api.metadata.event.action;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.global.util.N2oMapSerializer;
import net.n2oapp.framework.api.metadata.global.util.N2oNamespaceDeserializer;
import net.n2oapp.framework.api.metadata.global.util.N2oNamespaceSerializer;

import java.util.Map;

/**
 * Абстрактное действие
 */
@Getter
@Setter
public abstract class N2oAbstractAction implements N2oAction {
    private String id;
    private String src;
    private String namespaceUri;
    @JsonDeserialize(keyUsing = N2oNamespaceDeserializer.class)
    @JsonSerialize(keyUsing = N2oNamespaceSerializer.class, contentUsing = N2oMapSerializer.class)
    private Map<N2oNamespace, Map<String, String>> extAttributes;
}
