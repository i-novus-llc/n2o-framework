package net.n2oapp.framework.config.audit.listener;

import net.n2oapp.framework.api.event.N2oEventListener;
import net.n2oapp.framework.api.event.N2oStartedEvent;
import net.n2oapp.framework.config.audit.N2oConfigAudit;
import net.n2oapp.framework.config.audit.git.N2oGitCore;
import net.n2oapp.properties.StaticProperties;

/**
 * Рестарт конфигов, приведение директории с конфигами в рабочее состояние
 */
public class StartedAuditListener implements N2oEventListener<N2oStartedEvent> {
    private N2oConfigAudit configAudit;

    public StartedAuditListener(N2oConfigAudit configAudit) {
        this.configAudit = configAudit;
    }

    @Override
    public void handleEvent(N2oStartedEvent event) {
        configAudit.updateSystem();
        if (StaticProperties.getBoolean("n2o.config.audit.update.auto.enabled")) {
            configAudit.merge(N2oGitCore.SYSTEM_BRANCH_NAME, true);
            configAudit.setUpdatable(false);
        }
    }

}
