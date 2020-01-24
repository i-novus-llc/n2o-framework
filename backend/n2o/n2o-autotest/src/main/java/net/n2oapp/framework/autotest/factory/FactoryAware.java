package net.n2oapp.framework.autotest.factory;

public interface FactoryAware {
    ComponentFactory factory();
    void setFactory(ComponentFactory factory);
}
