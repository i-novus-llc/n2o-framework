package net.n2oapp.framework.autotest.impl.component;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.Component;

public abstract class N2oComponent implements Component {
    private SelenideElement element;

    @Override
    public SelenideElement element() {
        return element;
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

}
