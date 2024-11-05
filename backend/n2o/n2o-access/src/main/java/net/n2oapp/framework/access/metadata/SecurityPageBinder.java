package net.n2oapp.framework.access.metadata;

import net.n2oapp.framework.access.data.SecurityProvider;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.CompiledClassAware;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.compile.MetadataBinder;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.user.StaticUserContext;
import org.springframework.stereotype.Component;

import java.util.Map;

import static net.n2oapp.framework.access.metadata.Security.SECURITY_PROP_NAME;

/**
 * Проверка прав доступа на элемент Page
 */

@Component
public class SecurityPageBinder implements MetadataBinder<Page>, CompiledClassAware {

    private final SecurityProvider securityProvider;

    public SecurityPageBinder(SecurityProvider securityProvider) {
        this.securityProvider = securityProvider;
    }

    @Override
    public Page bind(Page compiled, BindProcessor p) {
        Map<String, Object> properties = compiled.getProperties();
        if (properties != null && properties.get(SECURITY_PROP_NAME) != null)
            securityProvider.checkAccess((Security) properties.get(SECURITY_PROP_NAME), StaticUserContext.getUserContext());
        return compiled;
    }

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return Page.class;
    }
}
