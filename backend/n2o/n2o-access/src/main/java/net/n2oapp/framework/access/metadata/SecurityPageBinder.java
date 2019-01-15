package net.n2oapp.framework.access.metadata;

import net.n2oapp.framework.access.data.SecurityProvider;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.CompiledClassAware;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.MetadataBinder;
import net.n2oapp.framework.api.metadata.meta.Page;
import net.n2oapp.framework.api.user.StaticUserContext;
import net.n2oapp.framework.api.user.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SecurityPageBinder implements MetadataBinder<Page>, CompiledClassAware {

    private SecurityProvider securityProvider;

    public SecurityPageBinder(@Autowired SecurityProvider securityProvider) {
        this.securityProvider = securityProvider;
    }

    @Override
    public Page bind(Page compiled, CompileProcessor p) {
        securityProvider.checkAccess(compiled, getUserContext());

        return compiled;
    }

    public UserContext getUserContext() {
        return StaticUserContext.getUserContext();
    }

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return Page.class;
    }
}
