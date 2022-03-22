package net.n2oapp.framework.api.metadata.event.action;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.jackson.ExtAttributesSerializer;

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
    @ExtAttributesSerializer
    private Map<N2oNamespace, Map<String, String>> extAttributes;
}
