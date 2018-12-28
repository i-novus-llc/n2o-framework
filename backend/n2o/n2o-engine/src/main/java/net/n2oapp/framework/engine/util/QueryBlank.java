package net.n2oapp.framework.engine.util;

import java.util.Map;

/**
 *  Базовая модель запроса
 */
public class QueryBlank {
    private String query;
    private Map<String,Object> args;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Map<String, Object> getArgs() {
        return args;
    }

    public void setArgs(Map<String, Object> args) {
        this.args = args;
    }
}

