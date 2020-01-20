package net.n2oapp.demo.model;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public interface ProtoClientSelectors extends BasePage {

    default SelenideElement getInputByLabel(String label) {
        SelenideElement selenideElement = $$(".modal-content").size() == 1 ? $(".modal-content") : $(".n2o-page");
        return selenideElement.$$("label").findBy(Condition.matchesText(label)).parent().find("input");
    }

    default SelenideElement getRadioByLabel(String label) {
        SelenideElement selenideElement = $$(".modal-content").size() == 1 ? $(".modal-content") : $(".n2o-page");
        return selenideElement.$$(".n2o-radio-container label").findBy(Condition.matchesText(label));
    }

    default SelenideElement getCheckboxByLabel(String label) {
        SelenideElement selenideElement = $$(".modal-content").size() == 1 ? $(".modal-content") : $(".n2o-page");
        return selenideElement.$$(".n2o-checkbox").findBy(Condition.matchesText(label)).find("label");
    }

    default SelenideElement getSaveButton() {
        return $$("button").findBy(Condition.text("Сохранить"));
    }

    default SelenideElement getCloseButton() {
        return $$("button").findBy(Condition.text("Закрыть"));
    }
}
