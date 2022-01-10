package net.n2oapp.framework.config.util;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.Submit;
import net.n2oapp.framework.api.metadata.dataprovider.N2oClientDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.saga.RefreshSaga;
import net.n2oapp.framework.api.metadata.meta.widget.RequestMethod;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.compile.widget.ModelsScope;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;

import java.util.Arrays;

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
        ModelsScope modelsScope = p.getScope(ModelsScope.class);
        CompiledQuery query = p.getCompiled(queryContext);
        p.addRoute(prepareQueryContextForRouteRegister(query));

        N2oClientDataProvider dataProvider = new N2oClientDataProvider();
        if (modelsScope != null) {
            dataProvider.setTargetModel(modelsScope.getModel());
            dataProvider.setTargetWidgetId(modelsScope.getWidgetId());
            queryContext.setFailAlertWidgetId(modelsScope.getWidgetId());
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
                    queryParam.setDatasource(preFilter.getDatasource());
                    if (queryParam.getDatasource() == null && preFilter.getRefWidgetId() != null) {
                        PageScope pageScope = p.getScope(PageScope.class);
                        queryParam.setDatasource(pageScope.getWidgetIdSourceDatasourceMap().get(preFilter.getRefWidgetId()));
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
        dataProvider.setTargetModel(ReduxModel.resolve);
        dataProvider.setPathParams(submit.getPathParams());
        dataProvider.setHeaderParams(submit.getHeaderParams());
        dataProvider.setFormParams(submit.getFormParams());

        N2oClientDataProvider.ActionContextData actionContextData = new N2oClientDataProvider.ActionContextData();
        actionContextData.setObjectId(compiledObject.getId());
        actionContextData.setOperationId(submit.getOperationId());
        actionContextData.setRoute(submit.getRoute());
        actionContextData.setMessageOnSuccess(p.cast(submit.getMessageOnSuccess(), false));
        actionContextData.setMessageOnFail(p.cast(submit.getMessageOnFail(), false));
        actionContextData.setMessagePosition(submit.getMessagePosition());
        actionContextData.setMessagePlacement(submit.getMessagePlacement());
        actionContextData.setOperation(compiledObject.getOperations().get(submit.getOperationId()));
        if (Boolean.TRUE.equals(submit.getRefreshOnSuccess())) {
            actionContextData.setRefresh(new RefreshSaga());
            if (submit.getRefreshDatasources() != null) {
                actionContextData.getRefresh().setDatasources(Arrays.asList(submit.getRefreshDatasources()));
            } else {
                if (widgetScope.getDatasourceId() != null)
                    actionContextData.getRefresh().setDatasources(Arrays.asList(widgetScope.getDatasourceId()));
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
