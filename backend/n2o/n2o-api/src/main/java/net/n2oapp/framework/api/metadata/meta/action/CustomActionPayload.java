package net.n2oapp.framework.api.metadata.meta.action;

import net.n2oapp.framework.api.metadata.local.util.StrictMap;

import java.util.Map;

/**
 * Настраиваемая полезная нагрузка действия
 */
public class CustomActionPayload extends StrictMap<String, Object> implements ActionPayload {

    public CustomActionPayload() {
    }

    public CustomActionPayload(Map<String, Object> params) {
        super();
        putAll(params);
    }
}
