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
            element().should(Condition.text(text));
        }

        @Override
        public void shouldHaveColor(Colors colors) {
            element().should(Condition.cssClass(colors.name("alert-")));
        }

        @Override
        public void shouldHavePlacement(Placement placement) {
            element().parent().should(Condition.cssClass(placement.name().toLowerCase()));
        }

        @Override
        public void shouldHaveStacktrace() {
            element().should(Condition.cssClass("with-details"));
        }
    }
}
