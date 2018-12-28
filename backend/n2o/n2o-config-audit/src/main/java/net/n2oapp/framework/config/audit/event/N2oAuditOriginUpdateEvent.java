package net.n2oapp.framework.config.audit.event;

import net.n2oapp.framework.api.event.N2oEvent;

/**
 * Событие изменений в центральном репозитории
 */
public class N2oAuditOriginUpdateEvent extends N2oEvent {
    public N2oAuditOriginUpdateEvent(Object source) {
        super(source);
    }
}
