package net.n2oapp.framework.autotest.impl.collection;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.collection.Alerts;
import net.n2oapp.framework.autotest.impl.component.field.N2oField;

public class N2oAlerts extends N2oComponentsCollection implements Alerts {

    @Override
    public Alert alert(int index) {
        return new N2oAlert(elements().get(index));
    }

    public static class N2oAlert extends N2oField implements Alert {

        public N2oAlert(SelenideElement element) {
            setElement(element);
        }

        @Override
        public void shouldHaveText(String text) {
            element().should(Condition.exist).waitUntil(Condition.text(text), 2000);
        }

        @Override
        public void shouldHaveColor(Colors colors) {
            element().should(Condition.exist).waitUntil(Condition.cssClass(colors.name("alert-")), 2000);
        }
    }
}
