package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.N2oAbstractMetaAction;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;
import net.n2oapp.framework.api.metadata.meta.saga.RedirectSaga;
import net.n2oapp.framework.api.metadata.meta.saga.RefreshSaga;
import net.n2oapp.framework.config.metadata.compile.ParentRouteScope;
import net.n2oapp.framework.config.metadata.compile.context.DialogContext;
import net.n2oapp.framework.config.metadata.compile.context.ModalPageContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;

import java.util.Arrays;
import java.util.stream.Collectors;

import static net.n2oapp.framework.config.register.route.RouteUtil.absolute;

/**
 * Абстрактная реализация компиляции действия, содержащего стандартные саги
 */
public abstract class AbstractMetaActionCompiler<D extends Action, S extends N2oAbstractMetaAction> extends AbstractActionCompiler<D, S> {

    @Override
    protected void initDefaults(S source, CompileContext<?, ?> context, CompileProcessor p) {
        super.initDefaults(source, context, p);
        source.setDoubleCloseOnSuccess(p.cast(source.getDoubleCloseOnSuccess(), false));
        source.setCloseOnSuccess(source.getDoubleCloseOnSuccess() || p.cast(source.getCloseOnSuccess(), false));
        source.setCloseOnFail(p.cast(source.getCloseOnFail(), false));
        source.setRefreshOnSuccess(p.cast(source.getRefreshOnSuccess(), true));
        source.setRefreshDatasources(initRefreshDatasources(source, p));
    }

    protected MetaSaga initSuccessMeta(D compiled, S source,
                                     CompileContext<?, ?> context, CompileProcessor p) {
        MetaSaga meta = new MetaSaga();
        boolean redirect = source.getRedirectUrl() != null;
        boolean doubleCloseOnSuccess = source.getDoubleCloseOnSuccess();
        boolean closeOnSuccess = source.getCloseOnSuccess() || doubleCloseOnSuccess;
        initCloseOnSuccess(context, meta, redirect, doubleCloseOnSuccess, closeOnSuccess);
        initRefreshOnClose(source, context, p, meta, closeOnSuccess);
        initRedirect(source, context, p, meta, redirect, doubleCloseOnSuccess);
        return meta;
    }

    protected MetaSaga initFailMeta(D compiled, S source,
                                  CompileContext<?, ?> context) {
        MetaSaga metaSaga = new MetaSaga();
        boolean closeOnFail = source.getCloseOnFail();
        if (closeOnFail) {
            if (context instanceof ModalPageContext || context instanceof DialogContext)
                metaSaga.setModalsToClose(1);
        }
        return metaSaga;
    }

    private void initCloseOnSuccess(CompileContext<?, ?> context, MetaSaga meta, boolean redirect, boolean doubleCloseOnSuccess, boolean closeOnSuccess) {
        if (closeOnSuccess) {
            if (context instanceof ModalPageContext || context instanceof DialogContext)
                meta.setModalsToClose(doubleCloseOnSuccess ? 2 : 1);
            else if (!redirect) {
                String backRoute;
                if (context instanceof PageContext) {
                    backRoute = ((PageContext) context).getParentRoute();
                } else {
                    backRoute = "/";
                }
                meta.setRedirect(new RedirectSaga());
                meta.getRedirect().setPath(backRoute);
                meta.getRedirect().setTarget(Target.application);
            }
        }
    }

    private void initRefreshOnClose(S source, CompileContext<?, ?> context, CompileProcessor p, MetaSaga meta, boolean closeOnSuccess) {
        if (source.getRefreshOnSuccess()) {
            if (context instanceof DialogContext) {
                meta.setRefresh(((DialogContext) context).getParentRefresh());
            } else {
                meta.setRefresh(new RefreshSaga());
                if (!closeOnSuccess && source.getRefreshDatasources() != null) {
                    PageScope pageScope = p.getScope(PageScope.class);
                    if (pageScope != null)
                        meta.getRefresh().setDatasources(Arrays.stream(source.getRefreshDatasources())
                                .map(pageScope::getClientDatasourceId).collect(Collectors.toList()));
                } else if (closeOnSuccess && PageContext.class.isAssignableFrom(context.getClass()) && ((PageContext) context).getRefreshClientDataSources() != null)
                    meta.getRefresh().setDatasources(((PageContext) context).getRefreshClientDataSources());
            }
        }
    }

    private void initRedirect(S source, CompileContext<?, ?> context, CompileProcessor p, MetaSaga meta, boolean redirect, boolean doubleCloseOnSuccess) {
        if (redirect) {
            if (context instanceof ModalPageContext || context instanceof DialogContext)
                meta.setModalsToClose(doubleCloseOnSuccess ? 2 : 1);
            if (context instanceof DialogContext) {
                meta.setRedirect(((DialogContext) context).getParentRedirect());
            } else {
                meta.setRedirect(new RedirectSaga());
                ParentRouteScope routeScope = p.getScope(ParentRouteScope.class);
                meta.getRedirect().setPath(absolute(source.getRedirectUrl(), routeScope != null ? routeScope.getUrl() : null));
                meta.getRedirect().setTarget(source.getRedirectTarget());
                meta.getRedirect().setServer(true);
            }
        }
    }

    private String[] initRefreshDatasources(N2oAbstractMetaAction source, CompileProcessor p) {
        if (source.getRefreshDatasources() != null)
            return source.getRefreshDatasources();
        String localDatasource = getLocalDatasource(p);
        if (localDatasource != null)
            return new String[]{localDatasource};
        return null;
    }
}
