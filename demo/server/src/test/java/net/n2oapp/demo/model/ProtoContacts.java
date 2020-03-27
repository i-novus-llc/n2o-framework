package net.n2oapp.demo.model;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.control.InputControl;
import net.n2oapp.framework.autotest.api.component.control.MaskedInputControl;
import net.n2oapp.framework.autotest.api.component.control.SelectControl;

/**
 * Страница "Контакты" ProtoContacts.page.xml
 */
public class ProtoContacts extends BaseSimplePage {

    public ProtoContacts() {
        super(false);
    }

    public ProtoContacts(boolean modal) {
        super(modal);
    }

    public void selectClient(String text) {
        getFields().field("Клиент").control(SelectControl.class).find(text);
        getFields().field("Клиент").control(SelectControl.class).select(Condition.text(text));
    }

    public void selectContactType(String text) {
        getFields().field("Тип контакта").control(SelectControl.class).openOptions();
        getFields().field("Тип контакта").control(SelectControl.class).select(Condition.text(text));
    }

    public MaskedInputControl getPhoneNumber() {
        return getFields().field("Номер телефона").control(MaskedInputControl.class);
    }

    public InputControl getDescription() {
        return getFields().field("Примечание").control(InputControl.class);
    }

}
