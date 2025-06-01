package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.action.N2oAbstractMetaAction;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.action.control.TargetEnum;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;
import net.n2oapp.framework.api.metadata.meta.saga.RedirectSaga;
import net.n2oapp.framework.api.metadata.meta.saga.RefreshSaga;
import net.n2oapp.framework.config.metadata.compile.ParentRouteScope;
import net.n2oapp.framework.config.metadata.compile.context.ModalPageContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;

import java.util.Arrays;

import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.register.route.RouteUtil.absolute;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceIds;

/**
 * Абстрактная реализация компиляции действия, содержащего стандартные саги
 */
public abstract class AbstractMetaActionCompiler<D extends Action, S extends N2oAbstractMetaAction> extends AbstractActionCompiler<D, S> {

    @Override
    protected void initDefaults(S source, CompileProcessor p) {
        super.initDefaults(source, p);
        source.setDoubleCloseOnSuccess(castDefault(source.getDoubleCloseOnSuccess(), false));
        source.setCloseOnSuccess(source.getDoubleCloseOnSuccess() || castDefault(source.getCloseOnSuccess(), false));
        source.setCloseOnFail(castDefault(source.getCloseOnFail(), false));
        source.setRefreshOnSuccess(castDefault(source.getRefreshOnSuccess(), true));
        source.setRefreshDatasourceIds(initRefreshDatasources(source, p));
    }

    protected MetaSaga initSuccessMeta(S source, CompileContext<?, ?> context, CompileProcessor p) {
        MetaSaga meta = new MetaSaga();
        boolean redirect = source.getRedirectUrl() != null;
        boolean doubleCloseOnSuccess = source.getDoubleCloseOnSuccess();
        boolean closeOnSuccess = source.getCloseOnSuccess() || doubleCloseOnSuccess;
        initCloseOnSuccess(context, meta, redirect, doubleCloseOnSuccess, closeOnSuccess);
        initRefreshOnClose(source, context, p, meta, closeOnSuccess);
        initRedirect(source, context, p, meta, redirect, doubleCloseOnSuccess);
        return meta;
    }

    protected MetaSaga initFailMeta(S source, CompileContext<?, ?> context) {
        MetaSaga metaSaga = new MetaSaga();
        if (Boolean.TRUE.equals(source.getCloseOnFail()) && context instanceof ModalPageContext)
            metaSaga.setModalsToClose(1);
        return metaSaga;
    }

    private void initCloseOnSuccess(CompileContext<?, ?> context, MetaSaga meta, boolean redirect, boolean doubleCloseOnSuccess, boolean closeOnSuccess) {
        if (closeOnSuccess) {
            if (context instanceof ModalPageContext)
                meta.setModalsToClose(doubleCloseOnSuccess ? 2 : 1);
            else if (!redirect) {
                String backRoute;
                if (context instanceof PageContext pageContext) {
                    backRoute = pageContext.getParentRoute();
                } else {
                    backRoute = "/";
                }
                meta.setRedirect(new RedirectSaga());
                meta.getRedirect().setPath(backRoute);
                meta.getRedirect().setTarget(TargetEnum.APPLICATION);
            }
        }
    }

    private void initRefreshOnClose(S source, CompileContext<?, ?> context, CompileProcessor p, MetaSaga meta, boolean closeOnSuccess) {
        if (Boolean.TRUE.equals(source.getRefreshOnSuccess())) {
            meta.setRefresh(new RefreshSaga());
            if (!closeOnSuccess && source.getRefreshDatasourceIds() != null) {
                PageScope pageScope = p.getScope(PageScope.class);
                if (pageScope != null)
                    meta.getRefresh().setDatasources(getClientDatasourceIds(Arrays.asList(source.getRefreshDatasourceIds()), p));
            } else if (closeOnSuccess && PageContext.class.isAssignableFrom(context.getClass()) && ((PageContext) context).getRefreshClientDataSourceIds() != null)
                meta.getRefresh().setDatasources(((PageContext) context).getRefreshClientDataSourceIds());
        }
    }

    private void initRedirect(S source, CompileContext<?, ?> context, CompileProcessor p, MetaSaga meta, boolean redirect, boolean doubleCloseOnSuccess) {
        if (redirect) {
            if (context instanceof ModalPageContext)
                meta.setModalsToClose(doubleCloseOnSuccess ? 2 : 1);
            meta.setRedirect(new RedirectSaga());
            ParentRouteScope routeScope = p.getScope(ParentRouteScope.class);
            meta.getRedirect().setPath(absolute(source.getRedirectUrl(), routeScope != null ? routeScope.getUrl() : null));
            meta.getRedirect().setTarget(source.getRedirectTarget());
            meta.getRedirect().setServer(true);
        }
    }

    private String[] initRefreshDatasources(N2oAbstractMetaAction source, CompileProcessor p) {
        if (source.getRefreshDatasourceIds() != null)
            return source.getRefreshDatasourceIds();
        String localDatasource = getLocalDatasourceId(p);
        if (localDatasource != null)
            return new String[]{localDatasource};
        return null;
    }
}
