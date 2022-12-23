package net.n2oapp.framework.api.metadata.action;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Исходная модель настраиваемого действия
 */
@Getter
@Setter
public class N2oCustomAction extends N2oAbstractMetaAction {
    private String type;
    private Map<String, String> payload;
}
