package net.n2oapp.framework.api.metadata.control;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.global.util.N2oMapSerializer;
import net.n2oapp.framework.api.metadata.global.util.N2oNamespaceDeserializer;
import net.n2oapp.framework.api.metadata.global.util.N2oNamespaceSerializer;

import java.util.Map;

/**
 * Исходная модель компонента
 */
@Getter
@Setter
public abstract class N2oComponent implements SourceComponent, ExtensionAttributesAware {
    private String namespaceUri;
    private String src;
    private String cssClass;
    private String style;
    @JsonDeserialize(keyUsing = N2oNamespaceDeserializer.class)
    @JsonSerialize(keyUsing = N2oNamespaceSerializer.class, contentUsing = N2oMapSerializer.class)
    private Map<N2oNamespace, Map<String, String>> extAttributes;
}
