package net.n2oapp.framework.api.metadata.event.action;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Исходная модель кастомного действия
 */
@Getter
@Setter
public class N2oCustomAction extends N2oAbstractMetaAction {
    private String type;
    private Map<String, String> payload;
}
