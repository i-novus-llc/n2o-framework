package net.n2oapp.framework.boot.graphql;

import net.n2oapp.framework.api.metadata.dataprovider.N2oGraphqlDataProvider;

import java.util.Map;

public interface GraphqlExecutor {

    Object execute(N2oGraphqlDataProvider invocation, Map<String, Object> data);
}
