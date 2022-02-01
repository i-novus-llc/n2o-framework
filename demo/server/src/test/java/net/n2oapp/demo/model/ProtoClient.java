package net.n2oapp.demo.model;

import net.n2oapp.framework.autotest.api.component.control.Checkbox;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.Select;
import net.n2oapp.framework.autotest.api.component.control.DateInput;
import net.n2oapp.framework.autotest.api.component.control.RadioGroup;

/**
 * Страница "Карточка клиента" ProtoClient.page.xml
 */
public class ProtoClient extends BaseSimplePage {

    public ProtoClient() {
        super(false);
    }

    public ProtoClient(boolean modal) {
        super(modal);
    }

    public InputText surname() {
        return getFields().field("Фамилия").control(InputText.class);
    }

    public InputText firstName() {
        return getFields().field("Имя").control(InputText.class);
    }

    public RadioGroup gender() {
        return getFields().field("Пол").control(RadioGroup.class);
    }

    public InputText patronymic() {
        return getFields().field("Отчество").control(InputText.class);
    }

    public void birthdayShouldHaveValue(String value) {
        getFields().field("Дата рождения").control(DateInput.class).shouldHaveValue(value);
    }

    public void birthdayValue(String value) {
        getFields().field("Дата рождения").control(DateInput.class).val(value);
    }

    public void birthdayShouldBeDisabled() {
        getFields().field("Дата рождения").control(DateInput.class).shouldBeDisabled();
    }

    public Checkbox getVIP() {
        return getFields().field("VIP").control(Checkbox.class);
    }

    public RadioGroup genderRadioGroup() {
        return getFields().field("Пол").control(RadioGroup.class);
    }

}
