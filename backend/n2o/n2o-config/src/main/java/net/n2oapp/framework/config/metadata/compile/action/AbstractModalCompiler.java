package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.action.N2oAbstractPageAction;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.modal.AbstractModal;
import net.n2oapp.framework.api.metadata.meta.action.modal.ModalPayload;
import net.n2oapp.framework.api.metadata.meta.saga.CloseSaga;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;
import net.n2oapp.framework.api.metadata.meta.saga.RefreshSaga;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static net.n2oapp.framework.config.register.route.RouteUtil.convertPathToId;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceIds;

/**
 * Компиляция абстрактного действия открытия окна
 */
public abstract class AbstractModalCompiler<D extends AbstractModal<? extends ModalPayload>, S extends N2oAbstractPageAction>
        extends AbstractOpenPageCompiler<D, S> {

    public void compileModal(S source, D compiled, CompileContext<?, ?> context, CompileProcessor p) {
        compiled.setObjectId(source.getObjectId());
        compiled.setOperationId(source.getOperationId());
        compiled.setPageId(source.getPageId());

        compileAction(compiled, source, p);
        PageContext pageContext = initPageContext(compiled, source, context, p);
        initOnCloseMeta(source, compiled, p);
        compilePayload(source, compiled, pageContext, p);
        initToolbarBySubmitOperation(source, pageContext, p);
    }

    protected abstract void compilePayload(S source, D compiled, PageContext pageContext, CompileProcessor p);

    @Override
    protected void initPageRoute(D compiled, String route, Map<String, ModelLink> pathMapping, Map<String, ModelLink> queryMapping) {
        ModalPayload payload = (ModalPayload) compiled.getPayload();
        String modalPageId = convertPathToId(route);
        payload.setName(modalPageId);
        payload.setPageId(modalPageId);
        payload.setPageUrl(route);
        payload.setPathMapping(pathMapping);
        payload.setQueryMapping(queryMapping);
    }

    /**
     * Инициализация саги для действия закрытия страницы
     *
     * @param compiled Клиентская модель открытия окна
     * @param p        Процессор сборки метаданных
     */
    private void initOnCloseMeta(S source, D compiled, CompileProcessor p) {
        if (!Boolean.TRUE.equals(source.getRefreshOnClose())) return;
        if (compiled.getMeta() == null)
            compiled.setMeta(new MetaSaga());
        compiled.getMeta().setOnClose(new CloseSaga());

        RefreshSaga refreshSaga = new RefreshSaga();
        List<String> refreshDsIds = new ArrayList<>();
        if (source.getRefreshDatasourceIds() == null) {
            String[] datasourceIds = getRefreshDatasourceId(source, p);
            if (datasourceIds != null)
                refreshDsIds.addAll(List.of(datasourceIds));
        } else {
            refreshDsIds.addAll(Arrays.asList(source.getRefreshDatasourceIds()));
        }
        refreshSaga.setDatasources(getClientDatasourceIds(refreshDsIds, p));
        compiled.getMeta().getOnClose().setRefresh(refreshSaga);
    }
}
