package net.n2oapp.framework.tutorial.helloworld.model;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.impl.component.snippet.N2oText;

/**
 * Страница Hello для автотестирования
 */
public class HelloPage {

    private final SimplePage simplePage;

    public HelloPage() {
        simplePage = N2oSelenide.page(SimplePage.class);
    }

    public FormWidget form() {
        return simplePage.widget(FormWidget.class);
    }

    public void helloShouldHaveText(String helloString) {
        N2oSelenide.component(form().fieldsets().fieldset(0).element().$(".n2o-text-field"), N2oText.class)
                .shouldHaveText(helloString);
    }
}
