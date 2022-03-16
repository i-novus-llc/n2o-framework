package net.n2oapp.framework.api.metadata.aware;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.global.util.N2oMapSerializer;
import net.n2oapp.framework.api.metadata.global.util.N2oNamespaceDeserializer;
import net.n2oapp.framework.api.metadata.global.util.N2oNamespaceSerializer;

import java.util.Map;

/**
 * Знание о дополнительных аттрибутах
 */
public interface ExtensionAttributesAware {
    @JsonDeserialize(keyUsing = N2oNamespaceDeserializer.class)
    @JsonSerialize(keyUsing = N2oNamespaceSerializer.class, contentUsing = N2oMapSerializer.class)
    Map<N2oNamespace, Map<String, String>> getExtAttributes();

    void setExtAttributes(Map<N2oNamespace, Map<String, String>> extAttributes);
}
