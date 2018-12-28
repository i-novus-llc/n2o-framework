package net.n2oapp.framework.config.audit.listener;

import net.n2oapp.framework.api.event.N2oEventListener;
import net.n2oapp.framework.config.audit.N2oConfigAudit;
import net.n2oapp.framework.config.audit.event.N2oAuditOriginUpdateEvent;

/**
 * Слушает иземенения в центральном репозитории
 */
public class AuditOriginUpdateListener implements N2oEventListener<N2oAuditOriginUpdateEvent> {
    private N2oConfigAudit configAudit;

    public AuditOriginUpdateListener(N2oConfigAudit configAudit) {
        this.configAudit = configAudit;
    }

    @Override
    public void handleEvent(N2oAuditOriginUpdateEvent event) {
        configAudit.pull();
    }

}
