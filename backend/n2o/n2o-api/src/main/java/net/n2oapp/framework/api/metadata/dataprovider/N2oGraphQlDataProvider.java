package net.n2oapp.framework.api.metadata.dataprovider;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oMapInvocation;

/**
 * Структура GraphQL провайдера данных
 */
@Getter
@Setter
public class N2oGraphQlDataProvider extends AbstractDataProvider implements N2oMapInvocation {

    private String endpoint;
    private String query;
    private String filterSeparator;
    private String sortingSeparator;
    private String pageMapping;
    private String sizeMapping;
    private String accessToken;
}
