package net.n2oapp.framework.config.audit.listener;

import net.n2oapp.framework.api.event.N2oEventListener;
import net.n2oapp.framework.config.audit.N2oConfigAudit;
import net.n2oapp.framework.config.register.XmlInfo;
import net.n2oapp.framework.config.register.audit.model.N2oConfigMessage;
import net.n2oapp.framework.config.register.event.MetadataRemovedEvent;

/**
 * Слушает удаление метаданых из регистра
 */
public class AuditConfigRemoveListener implements N2oEventListener<MetadataRemovedEvent> {
    private N2oConfigAudit configAudit;

    public AuditConfigRemoveListener(N2oConfigAudit configAudit) {
        this.configAudit = configAudit;
    }

    @Override
    public void handleEvent(MetadataRemovedEvent event) {
        if (configAudit.isEnabled()) {
            XmlInfo info = event.getInfo();
            configAudit.commit(info.getLocalPath(), N2oConfigMessage.DELETED_PREFIX.toString() + info.getLocalPath());
        }
    }

}
