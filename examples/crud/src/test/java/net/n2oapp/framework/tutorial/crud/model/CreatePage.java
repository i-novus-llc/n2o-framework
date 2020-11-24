package net.n2oapp.framework.tutorial.crud.model;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;

/**
 * Страница редактирования/создания Crud для автотестирования
 */
public class CreatePage {
    protected final Modal modal;

    public CreatePage() {
        this.modal = N2oSelenide.modal();
    }

    public void shouldHaveTitle(String text) {
        modal.shouldHaveTitle(text);
    }

    public InputText name() {
        return getFields().field("name").control(InputText.class);
    }

    public void save() {
        modal.toolbar().bottomRight().button("Сохранить").click();
    }

    protected Fields getFields() {
        return modal.content(SimplePage.class).widget(FormWidget.class).fields();
    }
}
