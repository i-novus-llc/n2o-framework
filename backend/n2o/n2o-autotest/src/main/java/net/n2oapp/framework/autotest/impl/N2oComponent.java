package net.n2oapp.framework.autotest.impl;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.component.Component;
import net.n2oapp.framework.autotest.factory.ComponentFactory;
import net.n2oapp.framework.autotest.factory.FactoryAware;

public class N2oComponent implements Component, FactoryAware {
    private SelenideElement element;
    private ComponentFactory factory;

    @Override
    public SelenideElement element() {
        return element;
    }

    @Override
    public ComponentFactory factory() {
        return factory;
    }

    @Override
    public void shouldExists() {
        element.should(Condition.exist);
    }

    @Override
    public void shouldNotExists() {
        element.shouldNot(Condition.exist);
    }

    @Override
    public void setElement(SelenideElement parentElement) {
        this.element = parentElement;
    }

    @Override
    public void setFactory(ComponentFactory factory) {
        this.factory = factory;
    }
}
