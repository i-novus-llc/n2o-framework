package net.n2oapp.framework.api.metadata.meta.action;

import net.n2oapp.framework.api.metadata.local.util.StrictMap;

import java.util.Map;

/**
 * Настраиваемая полезная нагрузка действия
 */
public class PerformActionPayload extends StrictMap<String, Object> implements ActionPayload {

    public PerformActionPayload() {
    }

    public PerformActionPayload(Map<String, Object> params) {
        super();
        putAll(params);
    }
}
