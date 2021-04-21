package net.n2oapp.framework.api.processing;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.ui.ActionRequestInfo;
import net.n2oapp.framework.api.ui.ActionResponseInfo;
import net.n2oapp.framework.api.ui.QueryRequestInfo;
import net.n2oapp.framework.api.ui.QueryResponseInfo;

/**
 * Обработка вызовов действий и выборок N2O
 */
public interface DataProcessing {

    /**
     * @param requestInfo  Информация о запросе
     * @param responseInfo Информация об ответе на запрос
     * @param dataSet      Входной набор данных
     */
    default void processAction(ActionRequestInfo<DataSet> requestInfo, ActionResponseInfo responseInfo, DataSet dataSet) {

    }

    /**
     * @param requestInfo  Информация о запросе
     * @param responseInfo Информация об ответе на запрос
     * @param dataSet      Входной набор данных
     */
    default void processActionError(ActionRequestInfo<DataSet> requestInfo, ActionResponseInfo responseInfo, DataSet dataSet) {

    }

    /**
     * @param requestInfo  Информация о запросе
     * @param responseInfo Информация об ответе на запрос
     * @param dataSet      Выходной набор данных
     */
    default void processActionResult(ActionRequestInfo<DataSet> requestInfo, ActionResponseInfo responseInfo, DataSet dataSet) {

    }

    /**
     * @param requestInfo  Информация о запросе
     * @param responseInfo Информация об ответе на запрос
     */
    default void processQuery(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo) {

    }

    /**
     * @param requestInfo  Информация о запросе
     * @param responseInfo Информация об ответе на запрос
     * @param exception    Ошибка при выполнении запроса
     */
    default void processQueryError(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo, N2oException exception) {

    }

    /**
     * @param requestInfo  Информация о запросе
     * @param responseInfo Информация об ответе на запрос
     * @param page         Выборка данных
     */
    default void processQueryResult(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo, CollectionPage<DataSet> page) {

    }
}
