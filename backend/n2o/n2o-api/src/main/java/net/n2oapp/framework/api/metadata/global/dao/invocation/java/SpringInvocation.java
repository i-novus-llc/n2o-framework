package net.n2oapp.framework.api.metadata.global.dao.invocation.java;

/**
 * Параметры для вызова методов spring бинов
 */
public class SpringInvocation extends JavaInvocation {
    private String beanId;

    public String getBeanId() {
        return beanId;
    }

    public void setBeanId(String beanId) {
        this.beanId = beanId;
    }

}
