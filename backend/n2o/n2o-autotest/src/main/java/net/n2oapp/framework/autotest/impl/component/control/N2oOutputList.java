package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import net.n2oapp.framework.api.metadata.meta.control.OutputList.Direction;
import net.n2oapp.framework.autotest.api.component.control.OutputList;

/**
 * Компонент вывода многострочного текста для автотестирования
 */
public class N2oOutputList extends N2oControl implements OutputList {
    @Override
    public void shouldBeEmpty() {
        element().shouldBe(Condition.empty);
    }

    @Override
    public void shouldHaveValue(String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void shouldHaveValues(String separator, String... values) {
        check(simpleItems()
                .filter(Condition.not(Condition.cssClass("n2o-output-list__item--link"))), separator, values);
    }

    @Override
    public void shouldHaveLinkValues(String separator, String... values) {
        check(linkedItems(), separator, values);
    }

    @Override
    public void shouldHaveDirection(Direction direction) {
        element().shouldHave(Condition.cssClass(String.format("n2o-output-list--%s", direction.name())));
    }

    @Override
    public void shouldHaveLink(String itemValue, String link) {
        linkedItems().find(Condition.text(itemValue))
                .shouldHave(Condition.attribute("href", link));
    }

    private void check(ElementsCollection elements, String separator, String... values) {
        elements.shouldHave(CollectionCondition.size(values.length));

        for (int i = 0; i < values.length - 1; i++) {
            elements.get(i).shouldHave(Condition.text(values[i]));
            elements.get(i).parent().$(".white-space-pre").shouldHave(Condition.text(separator));
        }

        elements.last().shouldHave(Condition.text(values[values.length - 1]));
        // если последний элемент в elements является последним среди всех, то проверяем сепаратор на пустоту
        if (element().lastChild().text().equals(elements.last().text()))
            elements.last().parent().$(".white-space-pre").shouldBe(Condition.empty);
    }

    protected ElementsCollection linkedItems() {
        return element().$$(".n2o-output-list__item--link");
    }

    protected ElementsCollection simpleItems() {
        return element().$$(".n2o-output-list__item");
    }
}
