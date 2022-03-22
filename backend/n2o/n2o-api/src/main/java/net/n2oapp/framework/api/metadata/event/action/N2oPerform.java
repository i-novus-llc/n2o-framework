package net.n2oapp.framework.api.metadata.event.action;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.jackson.ExtAttributesSerializer;

import java.util.Map;

/**
 * Исходная модель действия Perform
 */

@Getter
@Setter
public class N2oPerform extends N2oAbstractAction implements N2oAction, ExtensionAttributesAware {
    @ExtAttributesSerializer
    protected Map<N2oNamespace, Map<String, String>> extAttributes;
    private String type;

}
