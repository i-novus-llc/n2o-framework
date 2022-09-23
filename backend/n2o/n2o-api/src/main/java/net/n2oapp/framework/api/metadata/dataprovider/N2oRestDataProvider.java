package net.n2oapp.framework.api.metadata.dataprovider;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oMapInvocation;

import java.util.Set;

/**
 * Структура REST провайдера данных
 */
@Getter
@Setter
public class N2oRestDataProvider extends AbstractDataProvider implements N2oMapInvocation {
    private String query;
    private Method method;
    private String filtersSeparator;
    private String sortingSeparator;
    private String selectSeparator;
    private String joinSeparator;
    private String contentType;
    private String proxyHost;
    private Integer proxyPort;
    private String forwardedHeaders;
    private Set<String> forwardedHeadersSet;

    public enum Method {
        GET, PUT, POST, REMOVE, HEAD, DELETE, PATCH
    }

}
