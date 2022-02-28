package net.n2oapp.framework.boot.graphql;

import java.util.Map;

public interface GraphqlExecutor {

    Object execute(String query, String endpoint, Map<String, Object> data);
}
