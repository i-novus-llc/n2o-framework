package net.n2oapp.framework.api.rest;

import net.n2oapp.framework.api.factory.MetadataFactory;
import net.n2oapp.framework.api.ui.ActionRequestInfo;
import net.n2oapp.framework.api.ui.ActionResponseInfo;
import net.n2oapp.framework.api.ui.QueryRequestInfo;
import net.n2oapp.framework.api.ui.QueryResponseInfo;

/**
 * Фабрика GET/SET контроллеров данных
 */
public interface ControllerFactory extends MetadataFactory {

    /**
     * Выполнить запрос за данными
     * @param request Информация о запросе данных
     * @param response  Информация об ответе на запрос получения данных
     * @return Ответ на запрос получения данных
     */
    GetDataResponse execute(QueryRequestInfo request, QueryResponseInfo response);

    /**
     * Отправить данные
     * @param request Информация о запросе на отправку данных
     * @param response Информация об ответе на запрос на отправку данных
     * @return Ответ на запрос отправки данных
     */
    SetDataResponse execute(ActionRequestInfo request, ActionResponseInfo response);
}
