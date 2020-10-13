package net.n2oapp.framework.tutorial.greeting.model;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Alerts;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;

public class GreetingPage {

    private final SimplePage simplePage;

    public GreetingPage() {
        simplePage = N2oSelenide.page(SimplePage.class);
    }

    public void sendName(String name) {
        InputText inputText = form().fields().field("Имя").control(InputText.class);
        inputText.val(name);
        form().toolbar().bottomLeft().button("Отправить").click();
    }

    public Alerts alerts() {
        return simplePage.alerts();
    }

    private FormWidget form() {
        return simplePage.single().widget(FormWidget.class);
    }
}
