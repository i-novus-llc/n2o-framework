package net.n2oapp.demo.model;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;


public abstract class BaseSimplePage {

    protected final SimplePage simplePage;

    protected final Modal modal;

    public BaseSimplePage(boolean modal) {
        if (modal) {
            this.simplePage = null;
            this.modal = N2oSelenide.modal();
        } else {
            this.simplePage = N2oSelenide.page(SimplePage.class);
            this.modal = null;
        }
    }

    public void shouldHaveTitle(String text) {
        if (simplePage != null)
            simplePage.breadcrumb().crumb(1).shouldHaveLabel(text);
        else
            modal.shouldHaveTitle(text);
    }

    public void save() {
        if (simplePage != null)
            simplePage.toolbar().bottomRight().button("Сохранить").click();
        else
            modal.toolbar().bottomRight().button("Сохранить").click();
    }

    public void close() {
        if (simplePage != null)
            simplePage.toolbar().bottomRight().button("Закрыть").click();
        else
            modal.close();
    }

    protected Fields getFields() {
        if (simplePage != null)
            return simplePage.widget(FormWidget.class).fields();
        else
            return modal.content(SimplePage.class).widget(FormWidget.class).fields();
    }

}
