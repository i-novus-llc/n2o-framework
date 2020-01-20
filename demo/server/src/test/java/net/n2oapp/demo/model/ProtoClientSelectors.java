package net.n2oapp.demo.model;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$$;

public interface ProtoClientSelectors extends BasePage {
    default SelenideElement getInputByLabel(String label) {
        return $$("label").findBy(Condition.matchesText(label)).parent().find("input");
    }

    default SelenideElement getRadioByLabel(String label) {
        return $$(".n2o-radio-container label").findBy(Condition.matchesText(label));
    }

    default SelenideElement getCheckboxByLabel(String label) {
        return $$(".n2o-checkbox").findBy(Condition.matchesText(label)).find("label");
    }

    default SelenideElement getSaveButton() {
        return $$("button").findBy(Condition.text("Сохранить"));
    }
}
