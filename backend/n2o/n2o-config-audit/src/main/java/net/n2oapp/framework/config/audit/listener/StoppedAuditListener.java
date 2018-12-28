package net.n2oapp.framework.config.audit.listener;

import net.n2oapp.framework.api.event.N2oEventListener;
import net.n2oapp.framework.api.event.N2oStoppedEvent;
import net.n2oapp.framework.config.audit.N2oConfigAudit;

public class StoppedAuditListener implements N2oEventListener<N2oStoppedEvent> {
    private N2oConfigAudit configAudit;

    public StoppedAuditListener(N2oConfigAudit configAudit) {
        this.configAudit = configAudit;
    }

    @Override
    public void handleEvent(N2oStoppedEvent event) {
        configAudit.reestablish();
    }

}
