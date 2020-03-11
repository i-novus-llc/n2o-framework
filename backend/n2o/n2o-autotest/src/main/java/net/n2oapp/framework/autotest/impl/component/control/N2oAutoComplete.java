package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.control.AutoComplete;
import org.openqa.selenium.Keys;


/**
 * Компонент ввода текста с автозаполнением для автотестирования
 */
public class N2oAutoComplete extends N2oControl implements AutoComplete {

    @Override
    public void val(String value) {
        element().click();
        element().$(".n2o-inp").sendKeys(Keys.chord(Keys.CONTROL, "a"), value);
    }

    @Override
    public void shouldHaveValue(String value) {
        element().$(".n2o-inp").shouldHave(Condition.value(value));
    }

    @Override
    public void shouldHaveDropdownOptions(String... values) {
        element().parent().$$(".n2o-dropdown-control .text-cropped").shouldHave(CollectionCondition.texts(values));
    }

    @Override
    public void shouldNotHaveDropdownOptions() {
        element().parent().$$(".n2o-dropdown-control .text-cropped").shouldHaveSize(0);
    }

    @Override
    public void chooseDropdownOption(String value) {
        element().parent().$$(".n2o-dropdown-control button").find(Condition.text(value)).shouldBe(Condition.exist).click();
    }
}
