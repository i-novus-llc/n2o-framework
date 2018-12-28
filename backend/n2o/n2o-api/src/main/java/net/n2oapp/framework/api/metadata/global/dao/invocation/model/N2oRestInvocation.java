package net.n2oapp.framework.api.metadata.global.dao.invocation.model;

import net.n2oapp.framework.api.metadata.global.dao.RestErrorMapping;

/**
 * Модель вызова rest сервиса
 */
@Deprecated
public class N2oRestInvocation implements N2oMapInvocation {

    private String query;
    private String method;
    private String proxyHost;
    private Integer proxyPort;
    private String dateFormat;
    private RestErrorMapping errorMapping;
    private String namespaceUri;

    @Override
    public String getNamespaceUri() {
        return namespaceUri;
    }

    @Override
    public void setNamespaceUri(String namespaceUri) {
        this.namespaceUri = namespaceUri;
    }

    public RestErrorMapping getErrorMapping() {
        return errorMapping;
    }

    public void setErrorMapping(RestErrorMapping errorMapping) {
        this.errorMapping = errorMapping;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public Integer getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(Integer proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getDateFormat() {
        return dateFormat;
    }
}
