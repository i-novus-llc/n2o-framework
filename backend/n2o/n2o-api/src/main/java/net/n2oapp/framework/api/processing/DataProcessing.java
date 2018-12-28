package net.n2oapp.framework.api.processing;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.ui.ActionRequestInfo;
import net.n2oapp.framework.api.ui.ActionResponseInfo;
import net.n2oapp.framework.api.ui.QueryRequestInfo;
import net.n2oapp.framework.api.ui.QueryResponseInfo;
import net.n2oapp.framework.api.user.UserContext;

/**
 * Обработка вызовов действий и выборок N2O
 * User: operhod
 * Date: 28.12.13
 * Time: 10:32
 */
public interface DataProcessing {

    /**
     * @param requestInfo  - информация о запросе
     * @param responseInfo - информация об ответе на запрос
     * @param dataSet      - входной набор данных
     */
    void processAction(ActionRequestInfo<DataSet> requestInfo, ActionResponseInfo responseInfo, DataSet dataSet);

    /**
     * @param requestInfo  - информация о запросе
     * @param responseInfo - информация об ответе на запрос
     * @param dataSet      - входной набор данных
     * @param exception    - ошибка при выполнение запроса
     */
    void processActionError(ActionRequestInfo<DataSet> requestInfo, ActionResponseInfo responseInfo, DataSet dataSet,
                                   N2oException exception);

    /**
     * @param requestInfo  - информация о запросе
     * @param responseInfo - информация об ответе на запрос
     * @param dataSet      - выходной набор данных
     */
    void processActionResult(ActionRequestInfo<DataSet> requestInfo, ActionResponseInfo responseInfo, DataSet dataSet);

    /**
     * @param requestInfo  - информация о запросе
     * @param responseInfo - информация об ответе на запрос
     */
    void processQuery(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo);

    /**
     * @param requestInfo  - информация о запросе
     * @param responseInfo - информация об ответе на запрос
     * @param exception    - ошибка при выполнении запроса
     */
    void processQueryError(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo, N2oException exception);

    /**
     * @param requestInfo  - информация о запросе
     * @param responseInfo - информация об ответе на запрос
     * @param page         - выборка данных
     */
    void processQueryResult(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo, CollectionPage<DataSet> page);
}
