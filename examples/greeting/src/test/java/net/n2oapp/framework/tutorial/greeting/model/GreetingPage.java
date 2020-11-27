package net.n2oapp.framework.tutorial.greeting.model;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;

/**
 * Страница Greeting для автотестирования
 */
public class GreetingPage {

    private final SimplePage simplePage;

    public GreetingPage() {
        simplePage = N2oSelenide.page(SimplePage.class);
    }

    public void sendName(String name) {
        FormWidget formWidget = simplePage.widget(FormWidget.class);
        InputText inputText = formWidget.fields().field("Имя").control(InputText.class);
        inputText.val(name);
        formWidget.toolbar().bottomLeft().button("Отправить").click();
    }

    public void greetingShouldHave(String text) {
        simplePage.alerts().alert(0).shouldHaveText(text);
    }
}
