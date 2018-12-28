package net.n2oapp.framework.api.metadata.aware;

import net.n2oapp.framework.api.N2oNamespace;

import java.util.Map;

/**
 * Знание о дополнительных аттрибутах
 */
public interface ExtensionAttributesAware {
    Map<N2oNamespace, Map<String, String>> getExtAttributes();
    void setExtAttributes(Map<N2oNamespace, Map<String, String>> extAttributes);
}
