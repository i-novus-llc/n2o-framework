package net.n2oapp.framework.api.metadata.dataprovider;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oMapInvocation;

/**
 * GraphQL провайдер данных
 */
@Getter
@Setter
public class N2oGraphqlDataProvider extends AbstractDataProvider implements N2oMapInvocation {

    private String endpoint;
    private String query;
}
