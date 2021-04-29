package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.action.modal.open_drawer.OpenDrawer;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

/**
 * Связывание действия open-drawer с данными
 */
@Component
public class OpenDrawerBinder implements BaseMetadataBinder<OpenDrawer> {

    @Override
    public OpenDrawer bind(OpenDrawer action, BindProcessor p) {
        String url = p.resolveUrl(action.getPayload().getPageUrl(), action.getPayload().getPathMapping(), action.getPayload().getQueryMapping());
        action.getPayload().setPageUrl(url);
        return action;
    }

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return OpenDrawer.class;
    }
}
