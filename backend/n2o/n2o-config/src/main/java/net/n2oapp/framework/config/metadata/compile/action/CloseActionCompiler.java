package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.N2oAnchor;
import net.n2oapp.framework.api.metadata.action.N2oCloseAction;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.meta.action.AbstractAction;
import net.n2oapp.framework.api.metadata.meta.action.close.CloseAction;
import net.n2oapp.framework.api.metadata.meta.action.close.CloseActionPayload;
import net.n2oapp.framework.api.metadata.meta.action.link.LinkActionImpl;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;
import net.n2oapp.framework.api.metadata.meta.saga.RedirectSaga;
import net.n2oapp.framework.api.metadata.meta.saga.RefreshSaga;
import net.n2oapp.framework.config.metadata.compile.context.ModalPageContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.context.SubPageContext;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

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
        if (context instanceof SubPageContext subPageContext) {
            throw new N2oException(String.format("В странице '%s', на которую ссылаются в регионе \"<sub-page>\", нельзя использовать действие \"<close>\"",
                    subPageContext.getPageName()));
        }
        return context instanceof ModalPageContext
                ? getCloseAction(source, context, p)
                : getLinkAction(source, context, p);
    }

    private static LinkActionImpl getLinkAction(N2oCloseAction source, CompileContext<?, ?> context, CompileProcessor p) {
        N2oAnchor anchor = new N2oAnchor();
        if (source.getRedirectUrl() != null) {
            anchor.setHref(source.getRedirectUrl());
            anchor.setTarget(source.getRedirectTarget());
        } else if (context instanceof PageContext pageContext) {
            String backRoute = pageContext.getParentRoute();
            anchor.setHref(castDefault(backRoute, "/"));
            anchor.setTarget(Target.application);
        } else {
            anchor.setHref("/");
            anchor.setTarget(Target.application);
        }
        LinkActionImpl compiled = p.compile(anchor, context);
        compiled.setRestore(Boolean.TRUE);

        return compiled;
    }

    private CloseAction getCloseAction(N2oCloseAction source, CompileContext<?, ?> context, CompileProcessor p) {
        initDefaults(source, context, p);
        CloseAction closeAction = new CloseAction();
        compileAction(closeAction, source, p);
        closeAction.setType(p.resolve(property("n2o.api.action.close.type"), String.class));
        closeAction.setMeta(initMeta(source, context, p));
        closeAction.setPayload(initPayload(source, context));

        return closeAction;
    }

    private static CloseActionPayload initPayload(N2oCloseAction source, CompileContext<?, ?> context) {
        CloseActionPayload payload = new CloseActionPayload();
        if (context instanceof ModalPageContext modalPageContext) {
            payload.setPageId(((PageContext) context).getClientPageId());
            payload.setPrompt(castDefault(source.getPrompt(), modalPageContext::getUnsavedDataPromptOnClose, () -> true));
        }

        return payload;
    }

    private MetaSaga initMeta(N2oCloseAction source, CompileContext<?, ?> context, CompileProcessor p) {
        MetaSaga meta = new MetaSaga();
        boolean isRefreshable = castDefault(source.getRefresh(),
                () -> p.resolve(property("n2o.api.action.close.refresh_on_close"), Boolean.class));
        if (source.getRedirectUrl() == null && (context instanceof ModalPageContext))
            meta.setModalsToClose(1);
        if (isRefreshable) {
            meta.setRefresh(new RefreshSaga());
            if (context instanceof PageContext pageContext)
                meta.getRefresh().setDatasources(pageContext.getRefreshClientDataSourceIds());
        }
        if (source.getRedirectUrl() != null) {
            meta.setRedirect(new RedirectSaga());
            meta.getRedirect().setPath(source.getRedirectUrl());
            meta.getRedirect().setTarget(source.getRedirectTarget());
        }
        return meta;
    }
}
