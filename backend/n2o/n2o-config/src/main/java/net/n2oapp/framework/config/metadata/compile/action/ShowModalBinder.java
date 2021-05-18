package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.action.modal.show_modal.ShowModal;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

/**
 * Связывание действия show-modal с данными
 */
@Component
public class ShowModalBinder implements BaseMetadataBinder<ShowModal> {

    @Override
    public ShowModal bind(ShowModal action, BindProcessor p) {
        String url = p.resolveUrl(action.getPayload().getPageUrl(), action.getPayload().getPathMapping(), action.getPayload().getQueryMapping());
        action.getPayload().setPageUrl(url);
        return action;
    }

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return ShowModal.class;
    }
}
