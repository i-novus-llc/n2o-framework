package net.n2oapp.framework.api.metadata.global.dao.invocation.java;

/**
 * Параметры для вызова методов ejb бинов
 */
@Deprecated
public class EjbInvocation extends JavaInvocation {
    private String beanId;
    private String uri;
    private String protocol;
    private String application;
    private String module;
    private String distinct;
    private Boolean statefull;

    public String getBeanId() {
        return beanId;
    }

    public void setBeanId(String beanId) {
        this.beanId = beanId;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getDistinct() {
        return distinct;
    }

    public void setDistinct(String distinct) {
        this.distinct = distinct;
    }

    public Boolean getStatefull() {
        return statefull;
    }

    public void setStatefull(Boolean statefull) {
        this.statefull = statefull;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }
}
