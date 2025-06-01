package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import net.n2oapp.framework.api.metadata.meta.control.OutputList.DirectionEnum;
import net.n2oapp.framework.autotest.api.component.control.OutputList;

import java.time.Duration;

/**
 * Компонент вывода многострочного текста для автотестирования
 */
public class N2oOutputList extends N2oControl implements OutputList {
    @Override
    public void shouldBeEmpty() {
        element().shouldBe(Condition.empty);
    }

    @Override
    public void shouldHaveValue(String value, Duration... duration) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void shouldHaveValues(String separator, String[] values, Duration... duration) {
        check(simpleItems()
                .filter(Condition.not(Condition.cssClass("n2o-output-list__item--link"))), separator, values, duration);
    }

    @Override
    public void shouldHaveLinkValues(String separator, String[] values, Duration... duration) {
        check(linkedItems(), separator, values, duration);
    }

    @Override
    public void shouldHaveDirection(DirectionEnum direction) {
        element().shouldHave(Condition.cssClass(String.format("n2o-output-list--%s", direction.getId())));
    }

    @Override
    public void shouldHaveLink(String itemValue, String link) {
        linkedItems().find(Condition.text(itemValue))
                .shouldHave(Condition.attribute("href", link));
    }

    private void check(ElementsCollection elements, String separator, String[] values, Duration... duration) {
        elements.shouldHave(CollectionCondition.size(values.length));

        for (int i = 0; i < values.length - 1; i++) {
            should(Condition.text(values[i]), elements.get(i), duration);
            should(Condition.text(separator), elements.get(i).parent().$(".white-space-pre"), duration);
        }

        should(Condition.text(values[values.length - 1]), elements.last(), duration);
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
