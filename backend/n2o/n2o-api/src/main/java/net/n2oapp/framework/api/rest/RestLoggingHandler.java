package net.n2oapp.framework.api.rest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

/**
 * Логирование ответов на rest запросы
 */
public interface RestLoggingHandler {

    /**
     * Логирование ответа
     *
     * @param status  статус ответа
     * @param method  метод запроса
     * @param query   адрес запроса
     * @param headers заголовки запроса
     */
    default void handle(int status, HttpMethod method, String query, HttpHeaders headers) {
    }

    /**
     * Логирование ошибок
     *
     * @param e       ошибка
     * @param method  метод запроса
     * @param query   адрес запроса
     * @param headers заголовки запроса
     */
    default void handleError(Exception e, HttpMethod method, String query, HttpHeaders headers) {
    }
}
