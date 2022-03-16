package net.n2oapp.framework.api.metadata.event.action;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.global.util.N2oMapSerializer;
import net.n2oapp.framework.api.metadata.global.util.N2oNamespaceDeserializer;
import net.n2oapp.framework.api.metadata.global.util.N2oNamespaceSerializer;

import java.util.Map;

/**
 * Исходная модель действия Perform
 */

@Getter
@Setter
public class N2oPerform extends N2oAbstractAction implements N2oAction, ExtensionAttributesAware {
    @JsonDeserialize(keyUsing = N2oNamespaceDeserializer.class)
    @JsonSerialize(keyUsing = N2oNamespaceSerializer.class, contentUsing = N2oMapSerializer.class)
    protected Map<N2oNamespace, Map<String, String>> extAttributes;
    private String type;

}
