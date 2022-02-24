package net.n2oapp.framework.api.metadata.event.action;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;

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
    @JsonIgnore //todo добавить в io и метод для десериализации в N2oWebSocketController
    private Map<N2oNamespace, Map<String, String>> extAttributes;
}
