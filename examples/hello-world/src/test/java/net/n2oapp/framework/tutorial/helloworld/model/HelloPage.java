package net.n2oapp.framework.tutorial.helloworld.model;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;

public class HelloPage {

    private final SimplePage simplePage;

    public HelloPage() {
        simplePage = N2oSelenide.page(SimplePage.class);
    }

    public FormWidget form() {
        return simplePage.single().widget(FormWidget.class);
    }

    public void helloShouldHaveText(String helloString) {
        form().fieldsets().fieldset(0).element().$(".n2o-text-field").shouldBe(Condition.text(helloString));
    }

}
