package net.n2oapp.framework.access.metadata;

import net.n2oapp.framework.access.data.SecurityProvider;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.CompiledClassAware;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.compile.MetadataBinder;
import net.n2oapp.framework.api.metadata.meta.Page;
import net.n2oapp.framework.api.user.StaticUserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Проверка прав доступа на элемент Page
 */

@Component
public class SecurityPageBinder implements MetadataBinder<Page>, CompiledClassAware {

    private SecurityProvider securityProvider;

    public SecurityPageBinder(@Autowired SecurityProvider securityProvider) {
        this.securityProvider = securityProvider;
    }

    @Override
    public Page bind(Page compiled, BindProcessor p) {
        Map<String, Object> properties = compiled.getProperties();
        if (properties != null && properties.containsKey("security") && ((Security) properties.get("security")).getSecurityMap() != null)
            securityProvider.checkAccess(((Security) properties.get("security")).getSecurityMap(), StaticUserContext.getUserContext());
        return compiled;
    }

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return Page.class;
    }
}
