package net.n2oapp.framework.config.audit.service;

import net.n2oapp.context.StaticSpringContext;
import net.n2oapp.framework.config.audit.N2oConfigAudit;
import net.n2oapp.framework.config.audit.git.N2oGitCore;

/**
 * Команды для аудита конфигураций
 */
public class ConfigAuditService {
    private N2oConfigAudit configAudit = StaticSpringContext.getBean(N2oConfigAudit.class);

    public void updateSystem(){
        configAudit.updateSystem();
        configAudit.merge(N2oGitCore.SYSTEM_BRANCH_NAME, true);
    }
}
