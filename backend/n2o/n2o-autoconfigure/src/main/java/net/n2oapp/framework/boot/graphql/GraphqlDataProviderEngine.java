package net.n2oapp.framework.boot.graphql;

import lombok.Setter;
import net.n2oapp.framework.api.data.MapInvocationEngine;
import net.n2oapp.framework.api.metadata.dataprovider.N2oGraphqlDataProvider;

import java.util.Map;

/**
 * GraphQL провайдер данных
 */
@Setter
public class GraphqlDataProviderEngine implements MapInvocationEngine<N2oGraphqlDataProvider> {

    private String endpoint;
    private GraphqlExecutor graphqlExecutor;

    @Override
    public Class<? extends N2oGraphqlDataProvider> getType() {
        return N2oGraphqlDataProvider.class;
    }

    @Override
    public Object invoke(N2oGraphqlDataProvider invocation, Map<String, Object> data) {
        return graphqlExecutor.execute(invocation.getQuery(), initEndpoint(invocation.getEndpoint()), data);
    }

    private String initEndpoint(String invocationEndpoint) {
        if (invocationEndpoint != null)
            return invocationEndpoint;
        return endpoint;
    }
}
