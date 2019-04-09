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
        if (context instanceof ModalPageContext) {
            CloseAction closeAction = new CloseAction();
            compileAction(closeAction, source, p);
            closeAction.getOptions().setType(p.resolve(property("n2o.api.action.close.type"), String.class));
            closeAction.getOptions().setMeta(initMeta(closeAction, source, context, p));
            CloseActionPayload payload = new CloseActionPayload();
            payload.setPageId(((ModalPageContext) context).getClientPageId());
            payload.setPrompt(p.cast(source.getPrompt(), true));
            closeAction.getOptions().setPayload(payload);
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
            return new AnchorCompiler().compile(anchor, context, p);//todo нужно получать компилятор из фабрики
        }
    }

    private MetaSaga initMeta(CloseAction closeAction, N2oCloseAction source, CompileContext<?, ?> context, CompileProcessor p) {
        MetaSaga meta = new MetaSaga();
        boolean refresh = p.cast(source.getRefreshOnClose(), false);
        boolean redirect = source.getRedirectUrl() != null;
        if (!redirect && (context instanceof ModalPageContext))
            meta.setCloseLastModal(true);
        if (refresh) {
            meta.setRefresh(new RefreshSaga());
            meta.getRefresh().setType(RefreshSaga.Type.widget);
            String refreshWidgetId = null;
            if (context instanceof PageContext) {
                refreshWidgetId = ((PageContext) context).getParentWidgetId();
            }
            meta.getRefresh().getOptions().setWidgetId(refreshWidgetId);
        }
        if (redirect) {
            meta.setRedirect(new RedirectSaga());
            meta.getRedirect().setPath(source.getRedirectUrl());
            meta.getRedirect().setTarget(source.getRedirectTarget());
        }
        return meta;
    }
}
