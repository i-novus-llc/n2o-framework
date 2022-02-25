package net.n2oapp.framework.boot.graphql;

import lombok.Setter;
import net.n2oapp.framework.api.data.MapInvocationEngine;
import net.n2oapp.framework.api.metadata.dataprovider.N2oGraphqlDataProvider;

import java.util.Map;

@Setter
public class GraphqlDataProviderEngine implements MapInvocationEngine<N2oGraphqlDataProvider> {

    private GraphqlExecutor graphqlExecutor;

    @Override
    public Class<? extends N2oGraphqlDataProvider> getType() {
        return N2oGraphqlDataProvider.class;
    }

    @Override
    public Object invoke(N2oGraphqlDataProvider invocation, Map<String, Object> data) {
        return graphqlExecutor.execute(invocation, data);
    }
}
