package net.n2oapp.framework.autotest.impl.component.application;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.application.Footer;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

import java.time.Duration;

/**
 * Компонент Footer для автотестирования
 */
public class N2oFooter extends N2oComponent implements Footer {

    public N2oFooter(SelenideElement element) {
        setElement(element);
    }

    @Override
    public void leftTextShouldHaveValue(String name, Duration... duration) {
        should(Condition.exactText(name), leftSideText(), duration);
    }

    @Override
    public void rightTextShouldHaveValue(String name, Duration... duration) {
        should(Condition.exactText(name), rightSideText(), duration);
    }

    protected SelenideElement leftSideText() {
        return element().$(".text-left");
    }

    protected SelenideElement rightSideText() {
        return element().$(".text-right");
    }
}
