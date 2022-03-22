package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.jackson.ExtAttributesSerializer;

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
    @ExtAttributesSerializer
    private Map<N2oNamespace, Map<String, String>> extAttributes;
}
