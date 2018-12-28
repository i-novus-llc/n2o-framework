package net.n2oapp.criteria.api.util;

import java.util.Map;

/**
 * User: operhod
 * Date: 17.04.14
 * Time: 12:46
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
