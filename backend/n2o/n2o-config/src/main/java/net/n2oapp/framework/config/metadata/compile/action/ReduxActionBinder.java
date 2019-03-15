package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.action.AbstractReduxAction;
import net.n2oapp.framework.api.metadata.meta.action.ActionPayload;
import net.n2oapp.framework.api.metadata.meta.saga.AsyncMetaSaga;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;
import net.n2oapp.framework.api.metadata.meta.saga.RedirectSaga;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Связывание процессинга действия с данными
 */
@Component
public class ReduxActionBinder implements BaseMetadataBinder<AbstractReduxAction<? extends ActionPayload, ? extends MetaSaga>> {
    @Override
    public AbstractReduxAction<? extends ActionPayload, ? extends MetaSaga> bind(AbstractReduxAction<? extends ActionPayload, ? extends MetaSaga> action,
                                                                                 BindProcessor p) {
        getRedirects(action).forEach(r -> r.setPath(p.resolveUrl(r.getPath(), r.getPathMapping(), r.getQueryMapping())));
        return action;
    }

    private List<RedirectSaga> getRedirects(AbstractReduxAction<? extends ActionPayload, ? extends MetaSaga> action) {
        if (action.getOptions() == null || action.getOptions().getMeta() == null)
            return Collections.emptyList();
        List<RedirectSaga> metas = new ArrayList<>();
        if (action.getOptions().getMeta().getRedirect() != null) {
            metas.add(action.getOptions().getMeta().getRedirect());
        }
        if (action.getOptions().getMeta() instanceof AsyncMetaSaga
                && (((AsyncMetaSaga) action.getOptions().getMeta()).getSuccess().getRedirect() != null)) {
            metas.add(((AsyncMetaSaga) action.getOptions().getMeta()).getSuccess().getRedirect());
        }
        return metas;
    }

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return AbstractReduxAction.class;
    }
}
