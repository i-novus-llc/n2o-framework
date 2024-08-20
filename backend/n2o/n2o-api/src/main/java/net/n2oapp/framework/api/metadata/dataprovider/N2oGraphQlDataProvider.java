package net.n2oapp.framework.api.metadata.dataprovider;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.dao.invocation.N2oMapInvocation;

/**
 * Структура GraphQL провайдера данных
 */
@Getter
@Setter
public class N2oGraphQlDataProvider extends AbstractDataProvider implements N2oMapInvocation {

    private String endpoint;
    private String query;
    private String filterSeparator;
    private String filterPrefix;
    private String filterSuffix;
    private String sortingSeparator;
    private String sortingPrefix;
    private String sortingSuffix;
    private String pageMapping;
    private String sizeMapping;
    private String accessToken;
    private String forwardedHeaders;
    private String forwardedCookies;
    private String[] enums;
}
