package net.n2oapp.framework.engine.data.rest;

import net.n2oapp.framework.api.rest.RestLoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

public class N2oRestLoggingHandler implements RestLoggingHandler {

    private static final Logger log = LoggerFactory.getLogger(SpringRestDataProviderEngine.class);

    @Override
    public void handleError(Exception e, HttpMethod method, String query, HttpHeaders headers) {
        log.error("Execution error with REST query: {}", query, e);
    }
}
