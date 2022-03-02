package net.n2oapp.framework.boot.graphql;

import java.util.Map;

/**
 * Интерфейс для отправки GraphQL запросов на удаленный сервер
 */
public interface GraphqlExecutor {

    /**
     * Отправка и получение данных с удаленного GraphQL сервера
     * @param query    GraphQL запрос
     * @param endpoint URL удаленного сервера
     * @param data     Данные выборки
     * @return         Объект данных с удаленного сервера
     */
    Object execute(String query, String endpoint, Map<String, Object> data);
}
