package net.n2oapp.framework.api.rest;

import net.n2oapp.framework.api.factory.MetadataFactory;
import net.n2oapp.framework.api.ui.*;

/**
 * Фабрика GET/SET контроллеров данных
 */
public interface ControllerFactory extends MetadataFactory {

    /**
     * Выполнить запрос за данными
     *
     * @param request  Информация о запросе данных
     * @param response Информация об ответе на запрос получения данных
     * @return Ответ на запрос получения данных
     */
    GetDataResponse execute(QueryRequestInfo request, QueryResponseInfo response);

    /**
     * Отправить данные
     *
     * @param request  Информация о запросе на отправку данных
     * @param response Информация об ответе на запрос на отправку данных
     * @return Ответ на запрос отправки данных
     */
    SetDataResponse execute(ActionRequestInfo request, ActionResponseInfo response);

    /**
     * Проверить данные
     *
     * @param request  Информация о запросе на проверку данных
     * @param response Информация об ответе на запрос на проверку данных
     * @return Ответ на запрос проверки данных
     */
    ValidationDataResponse execute(ValidationRequestInfo request, ValidationResponseInfo response);
}
