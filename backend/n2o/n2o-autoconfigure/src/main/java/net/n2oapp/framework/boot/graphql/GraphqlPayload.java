package net.n2oapp.framework.boot.graphql;

import org.json.JSONObject;

public class GraphqlPayload {

    private static final String QUERY = "query";
    private static final String VARIABLES = "variables";
    private JSONObject payload = new JSONObject();

    public void setQuery(String query) {
        payload.put(QUERY, query);
    }

    public void setVariables(String variables) {
        payload.put(VARIABLES, variables);
    }

    @Override
    public String toString() {
        return payload.toString();
    }
}
