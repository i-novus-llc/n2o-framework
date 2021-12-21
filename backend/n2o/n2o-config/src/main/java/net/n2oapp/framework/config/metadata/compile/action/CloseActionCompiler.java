package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.N2oAnchor;
import net.n2oapp.framework.api.metadata.event.action.N2oCloseAction;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.meta.action.AbstractAction;
import net.n2oapp.framework.api.metadata.meta.action.close.CloseAction;
import net.n2oapp.framework.api.metadata.meta.action.close.CloseActionPayload;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;
import net.n2oapp.framework.api.metadata.meta.saga.RedirectSaga;
import net.n2oapp.framework.api.metadata.meta.saga.RefreshSaga;
import net.n2oapp.framework.config.metadata.compile.context.DialogContext;
import net.n2oapp.framework.config.metadata.compile.context.ModalPageContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Сборка действия закрытия страницы
 */
@Component
public class CloseActionCompiler extends AbstractActionCompiler<AbstractAction, N2oCloseAction> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oCloseAction.class;
    }

    @Override
    public AbstractAction compile(N2oCloseAction source, CompileContext<?, ?> context, CompileProcessor p) {
        if (context instanceof ModalPageContext || context instanceof DialogContext) {
            initDefaults(source, context, p);
            CloseAction closeAction = new CloseAction();
            compileAction(closeAction, source, p);
            closeAction.setType(p.resolve(property("n2o.api.action.close.type"), String.class));
            closeAction.setMeta(initMeta(source, context, p));
            CloseActionPayload payload = new CloseActionPayload();
            if (context instanceof ModalPageContext) {
                payload.setPageId(((PageContext) context).getClientPageId());
                payload.setPrompt(p.cast(((PageContext) context).getUnsavedDataPromptOnClose(), source.getPrompt(), true));
            } else {
                payload.setPageId(((DialogContext) context).getParentWidgetId());
            }
            closeAction.setPayload(payload);
            return closeAction;
        } else {
            N2oAnchor anchor = new N2oAnchor();
            if (source.getRedirectUrl() != null) {
                anchor.setHref(source.getRedirectUrl());
                anchor.setTarget(source.getRedirectTarget());
            } else if (context instanceof PageContext) {
                String backRoute = ((PageContext) context).getParentRoute();
                anchor.setHref(p.cast(backRoute, "/"));
                anchor.setTarget(Target.application);
            } else {
                anchor.setHref("/");
                anchor.setTarget(Target.application);
            }
            return p.compile(anchor, context);
        }
    }

    private MetaSaga initMeta(N2oCloseAction source, CompileContext<?, ?> context, CompileProcessor p) {
        MetaSaga meta = new MetaSaga();
        boolean refresh = p.cast(Boolean.TRUE.equals(source.getRefreshOnClose())
                || Boolean.TRUE.equals(source.getRefresh()), p.resolve(property("n2o.api.action.close.refresh_on_close"), Boolean.class));
        boolean redirect = source.getRedirectUrl() != null;
        if (!redirect && (context instanceof ModalPageContext))
            meta.setModalsToClose(1);
        if (refresh) {
            meta.setRefresh(new RefreshSaga());
            if (context instanceof PageContext)
                meta.getRefresh().setDatasources(((PageContext) context).getRefreshClientDataSources());
        }
        if (redirect) {
            meta.setRedirect(new RedirectSaga());
            meta.getRedirect().setPath(source.getRedirectUrl());
            meta.getRedirect().setTarget(source.getRedirectTarget());
        }
        return meta;
    }
}
