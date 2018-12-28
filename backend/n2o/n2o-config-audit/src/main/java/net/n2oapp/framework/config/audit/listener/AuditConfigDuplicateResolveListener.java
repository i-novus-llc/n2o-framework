package net.n2oapp.framework.config.audit.listener;

import net.n2oapp.framework.api.event.N2oEventListener;
import net.n2oapp.framework.config.audit.N2oConfigAudit;
import net.n2oapp.framework.config.register.audit.model.N2oConfigMessage;
import net.n2oapp.framework.config.register.event.ConfigDuplicateResolveEvent;

/**
 * Слушает разрешение дубликатов и делает комит
 */
public class AuditConfigDuplicateResolveListener implements N2oEventListener<ConfigDuplicateResolveEvent> {
    private N2oConfigAudit configAudit;

    public AuditConfigDuplicateResolveListener(N2oConfigAudit configAudit) {
        this.configAudit = configAudit;
    }

    @Override
    public void handleEvent(ConfigDuplicateResolveEvent event) {
        configAudit.commitAll(N2oConfigMessage.DUPLICATE_DELETED_PREFIX + event.getInfo().getId());
    }
}
