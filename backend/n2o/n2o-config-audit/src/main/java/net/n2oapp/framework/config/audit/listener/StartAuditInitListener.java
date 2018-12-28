package net.n2oapp.framework.config.audit.listener;

import net.n2oapp.framework.config.audit.N2oConfigAudit;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * Инициализация аудита при старте приложения
 * */
public class StartAuditInitListener implements ApplicationListener<ContextRefreshedEvent> {
    private N2oConfigAudit configAudit;

    public StartAuditInitListener(N2oConfigAudit configAudit) {
        this.configAudit = configAudit;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        configAudit.init();
    }
}
