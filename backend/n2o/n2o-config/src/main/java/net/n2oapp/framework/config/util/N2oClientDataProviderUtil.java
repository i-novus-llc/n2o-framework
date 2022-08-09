package net.n2oapp.framework.config.util;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.Submit;
import net.n2oapp.framework.api.metadata.dataprovider.N2oClientDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.saga.RefreshSaga;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePlacement;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePosition;
import net.n2oapp.framework.api.metadata.meta.widget.RequestMethod;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;
import static net.n2oapp.framework.config.util.QueryContextUtil.prepareQueryContextForRouteRegister;

/**
 * Утилита для инициализации исходной модели клиентского провайдера данных
 */
public class N2oClientDataProviderUtil {

    private N2oClientDataProviderUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Инициализация провайдера данных клиента по данным поля с выборкой
     *
     * @param preFilters Массив префильтров
     * @param queryId    Идентификатор выборки
     * @param p          Процессор сборки метаданных
     * @return Инициализированный провайдер данных клиента
     */
    public static N2oClientDataProvider initFromField(N2oPreFilter[] preFilters, String queryId, CompileProcessor p) {
        QueryContext queryContext = new QueryContext(queryId);
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        CompiledQuery query = p.getCompiled(queryContext);
        p.addRoute(prepareQueryContextForRouteRegister(query));

        N2oClientDataProvider dataProvider = new N2oClientDataProvider();
        if (widgetScope != null) {
            dataProvider.setTargetModel(widgetScope.getModel());
            dataProvider.setClientDatasourceId(widgetScope.getClientDatasourceId());
        }
        dataProvider.setUrl(query.getRoute());

        if (preFilters != null) {
            N2oParam[] queryParams = new N2oParam[preFilters.length];
            for (int i = 0; i < preFilters.length; i++) {
                N2oPreFilter preFilter = preFilters[i];
                N2oQuery.Filter filter = query.getFilterByPreFilter(preFilter);
                N2oParam queryParam = new N2oParam();
                queryParam.setName(query.getFilterIdToParamMap().get(filter.getFilterField()));
                if (preFilter.getParam() == null) {
                    queryParam.setValueList(getPrefilterValue(preFilter));
                    queryParam.setModel(preFilter.getModel());
                    queryParam.setDatasourceId(preFilter.getDatasourceId());
                    if (queryParam.getDatasourceId() == null && preFilter.getRefWidgetId() != null) {
                        PageScope pageScope = p.getScope(PageScope.class);
                        queryParam.setDatasourceId(pageScope.getWidgetIdSourceDatasourceMap().get(preFilter.getRefWidgetId()));
                    }
                } else {
                    queryParam.setValueParam(preFilter.getParam());
                }
                queryParams[i] = queryParam;


            }
            dataProvider.setQueryParams(queryParams);
        }
        return dataProvider;
    }

    /**
     * Инициализация провайдера данных клиента по метаданной, содержащей действие submit
     *
     * @param submit         Действие отправки данных
     * @param fieldId        Идентификатор поля
     * @param compiledObject Скомпилированный объект
     * @param p              Процессор сборки метаданных
     * @return Инициализированный провайдер данных клиента
     */
    public static N2oClientDataProvider initFromSubmit(Submit submit, String fieldId,
                                                       CompiledObject compiledObject, CompileProcessor p) {
        if (compiledObject == null)
            throw new N2oException(String.format("For compilation submit for field [%s] is necessary object!", fieldId));

        N2oClientDataProvider dataProvider = new N2oClientDataProvider();
        dataProvider.setMethod(RequestMethod.POST);
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        dataProvider.setUrl(p.cast(submit.getRoute(), widgetScope.getDatasourceId()));
        dataProvider.setTargetModel(widgetScope.getModel());
        dataProvider.setClientDatasourceId(widgetScope.getClientDatasourceId());
        dataProvider.setPathParams(submit.getPathParams());
        dataProvider.setHeaderParams(submit.getHeaderParams());
        dataProvider.setFormParams(submit.getFormParams());

        N2oClientDataProvider.ActionContextData actionContextData = new N2oClientDataProvider.ActionContextData();
        actionContextData.setObjectId(compiledObject.getId());
        actionContextData.setOperationId(submit.getOperationId());
        actionContextData.setRoute(submit.getRoute());
        actionContextData.setMessageOnSuccess(p.cast(submit.getMessageOnSuccess(), false));
        actionContextData.setMessageOnFail(p.cast(submit.getMessageOnFail(), false));
        actionContextData.setMessagePosition(p.cast(submit.getMessagePosition(),
                p.resolve(property("n2o.api.message.position"), MessagePosition.class)));
        actionContextData.setMessagePlacement(p.cast(submit.getMessagePlacement(),
                p.resolve(property("n2o.api.message.placement"), MessagePlacement.class)));
        actionContextData.setMessagesForm(submit.getMessageWidgetId());
        actionContextData.setOperation(compiledObject.getOperations().get(submit.getOperationId()));
        if (Boolean.TRUE.equals(submit.getRefreshOnSuccess())) {
            actionContextData.setRefresh(new RefreshSaga());
            if (submit.getRefreshDatasourceIds() != null) {
                actionContextData.getRefresh().setDatasources(Arrays.stream(submit.getRefreshDatasourceIds())
                        .map(d -> getClientDatasourceId(d, p)).collect(Collectors.toList()));
            } else {
                if (widgetScope.getClientDatasourceId() != null)
                    actionContextData.getRefresh().setDatasources(Collections.singletonList(widgetScope.getClientDatasourceId()));
            }
        }
        dataProvider.setActionContextData(actionContextData);

        return dataProvider;
    }

    private static Object getPrefilterValue(N2oPreFilter n2oPreFilter) {
        if (n2oPreFilter.getValues() == null) {
            return ScriptProcessor.resolveExpression(n2oPreFilter.getValue());
        } else {
            return ScriptProcessor.resolveArrayExpression(n2oPreFilter.getValues());
        }
    }
}
