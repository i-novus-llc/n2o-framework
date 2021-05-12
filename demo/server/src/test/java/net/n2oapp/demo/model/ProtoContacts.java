package net.n2oapp.demo.model;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.control.InputSelect;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.MaskedInput;

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

    public void selectContactType(String text) {
        getFields().field("Тип контакта").control(InputSelect.class).select(Condition.text(text));
    }

    public void shouldHaveContactType(String text) {
        getFields().field("Тип контакта").control(InputSelect.class).shouldSelected(text);
    }

    public MaskedInput getPhoneNumber() {
        return getFields().field("Моб. телефон").control(MaskedInput.class);
    }

    public InputText getDescription() {
        return getFields().field("Примечание").control(InputText.class);
    }

}
