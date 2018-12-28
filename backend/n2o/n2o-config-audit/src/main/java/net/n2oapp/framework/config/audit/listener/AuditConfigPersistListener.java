package net.n2oapp.framework.config.audit.listener;

import net.n2oapp.framework.api.event.N2oEventListener;
import net.n2oapp.framework.config.audit.N2oConfigAudit;
import net.n2oapp.framework.config.register.Origin;
import net.n2oapp.framework.config.register.audit.model.N2oConfigMessage;
import net.n2oapp.framework.config.register.event.ConfigPersistEvent;

/**
 * Слушает добавление и изменение метаданных в регистре.
 */
public class AuditConfigPersistListener implements N2oEventListener<ConfigPersistEvent> {
    private N2oConfigAudit configAudit;

    public AuditConfigPersistListener(N2oConfigAudit configAudit) {
        this.configAudit = configAudit;
    }

    @Override
    public void handleEvent(ConfigPersistEvent event) {
        if (configAudit.isEnabled()) {
            if (Origin.xml.equals(event.getInfo().getOrigin())) {
                String message;
                if (event.isCreate()) {
                    message = N2oConfigMessage.CREATED_PREFIX.toString() + event.getInfo().getLocalPath();
                } else {
                    message = N2oConfigMessage.UPDATED_PREFIX.toString() + event.getInfo().getLocalPath();
                }
                configAudit.commit(event.getInfo().getLocalPath(), message);
            }

        }
    }

}
