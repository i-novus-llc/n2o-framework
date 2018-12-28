package net.n2oapp.framework.engine.rest;

import java.util.Map;

/**
 * @author operehod
 * @since 30.05.2015
 */
public interface RestProcessing {

    /**
     * @param method - http-метод
     * @param query - запрос
     * @param args - параметры запроса (для GET это path-параметры, для POST это параметр json-body)
     * @param headers - хедеры
     */
    public void process(String method, String query, Map<String, Object> args, Map<String, String> headers);

}
