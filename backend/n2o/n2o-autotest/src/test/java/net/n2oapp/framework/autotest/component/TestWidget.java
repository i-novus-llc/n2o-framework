package net.n2oapp.framework.autotest.component;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.component.widget.Widget;
import net.n2oapp.framework.autotest.impl.N2oComponent;

public class TestWidget extends N2oComponent implements Widget {
    public void textShouldBe(String text) {
        element().shouldBe(Condition.text(text));
    }
}
