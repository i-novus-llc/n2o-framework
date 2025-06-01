package net.n2oapp.framework.api.metadata.dataprovider;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.dao.invocation.N2oMapInvocation;

/**
 * Структура REST провайдера данных
 */
@Getter
@Setter
public class N2oRestDataProvider extends AbstractDataProvider implements N2oMapInvocation {
    private String query;
    private MethodEnum method;
    private String filtersSeparator;
    private String sortingSeparator;
    private String selectSeparator;
    private String joinSeparator;
    private String proxyHost;
    private Integer proxyPort;
    private String forwardedHeaders;
    private String forwardedCookies;

    public enum MethodEnum {
        GET, PUT, POST, REMOVE, HEAD, DELETE, PATCH
    }
}