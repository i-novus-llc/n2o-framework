package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.action.AbstractAction;
import net.n2oapp.framework.api.metadata.meta.action.ActionPayload;
import net.n2oapp.framework.api.metadata.meta.saga.AsyncMetaSaga;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;
import net.n2oapp.framework.api.metadata.meta.saga.RedirectSaga;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * Связывание процессинга действия с данными
 */
@Component
public class ReduxActionBinder implements BaseMetadataBinder<AbstractAction<? extends ActionPayload, ? extends MetaSaga>> {
    @Override
    public AbstractAction<? extends ActionPayload, ? extends MetaSaga> bind(AbstractAction<? extends ActionPayload, ? extends MetaSaga> action,
                                                                            BindProcessor p) {
        bindRedirects(action, p);
        return action;
    }

    private void bindRedirects(AbstractAction<? extends ActionPayload, ? extends MetaSaga> action, BindProcessor p) {
        if (action.getMeta() == null)
            return;
        RedirectSaga redirect = action.getMeta().getRedirect();
        if (redirect != null) {
            RedirectSaga redirectSaga = getRedirectSaga(p, redirect);
            action.getMeta().setRedirect(redirectSaga);
        }
        if (action.getMeta() instanceof AsyncMetaSaga
                && (((AsyncMetaSaga) action.getMeta()).getSuccess().getRedirect() != null)) {
            RedirectSaga redirectSaga = getRedirectSaga(p, ((AsyncMetaSaga) action.getMeta()).getSuccess().getRedirect());
            ((AsyncMetaSaga) action.getMeta()).getSuccess().setRedirect(redirectSaga);
        }
    }

    private RedirectSaga getRedirectSaga(BindProcessor p, RedirectSaga redirect) {
        RedirectSaga redirectSaga = new RedirectSaga();
        redirectSaga.setPath(p.resolveUrl(redirect.getPath(), redirect.getPathMapping(), redirect.getQueryMapping()));
        redirectSaga.setPathMapping(new HashMap<>(redirect.getPathMapping()));
        redirectSaga.setQueryMapping(new HashMap<>(redirect.getQueryMapping()));
        redirectSaga.setTarget(redirect.getTarget());
        redirectSaga.setServer(redirect.isServer());
        return redirectSaga;
    }

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return AbstractAction.class;
    }
}
