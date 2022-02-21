package net.n2oapp.framework.autotest.impl.component;

import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.Element;

/**
 * Абстрактная реализация html элемента
 */
public abstract class N2oElement implements Element {

    private SelenideElement element;

    @Override
    public SelenideElement element() {
        return element;
    }

    @Override
    public void setElement(SelenideElement element) {
        this.element = element;
    }
}
