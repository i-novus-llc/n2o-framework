package net.n2oapp.demo.model;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public interface ProtoClientSelectors extends BasePage {

    default SelenideElement getInputByLabel(String label) {
        SelenideElement selenideElement = $$(".modal-content").size() == 1 ? $(".modal-content") : $(".n2o-page");
        return selenideElement.$$(".n2o-field-label").findBy(Condition.matchesText(label)).parent().find("input");
    }

    default SelenideElement getInputSelectByLabel(String label) {
        SelenideElement selenideElement = $$(".modal-content").size() == 1 ? $(".modal-content") : $(".n2o-page");
        return selenideElement.$$("label").findBy(Condition.matchesText(label)).parent().find("div");
    }

    default SelenideElement getRadioByLabel(String label) {
        SelenideElement selenideElement = $$(".modal-content").size() == 1 ? $(".modal-content") : $(".n2o-page");
        return selenideElement.$$(".n2o-radio-container label").findBy(Condition.matchesText(label));
    }

    default SelenideElement getCheckboxByLabel(String label) {
        SelenideElement selenideElement = $$(".modal-content").size() == 1 ? $(".modal-content") : $(".n2o-page");
        return selenideElement.$$(".n2o-checkbox").findBy(Condition.matchesText(label)).find("label");
    }

    default SelenideElement getModalTitle() {
        if ($$(".modal-content").size() == 1)
            return $(".modal-title");
        return null;
    }

    default SelenideElement getSaveButton() {
        return $$("button").findBy(Condition.text("Сохранить"));
    }

    default SelenideElement getCloseButton() {
        return $$(".btn-group a").find(Condition.text("Закрыть"));
    }

    default SelenideElement getCloseCrossButton() {
        return Selenide.$$("button").findBy(Condition.attribute("class", "close"));
    }
}
