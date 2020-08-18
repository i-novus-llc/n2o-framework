package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
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
        check(element().$$(".n2o-output-list__text"), separator, values);
    }

    @Override
    public void shouldHaveLinkValues(String separator, String... values) {
        check(element().$$(".n2o-output-list__link"), separator, values);
    }

    @Override
    public void shouldHaveDirection(net.n2oapp.framework.api.metadata.meta.control.OutputList.Direction direction) {
        element().shouldHave(Condition.cssClass("n2o-output-list_" + direction.name() + "_direction"));
    }

    @Override
    public void linkShouldHaveValue(String itemValue, String link) {
        element().$$(".n2o-output-list__link").find(Condition.text(itemValue))
                .shouldHave(Condition.attribute("href", link));
    }

    private void check(ElementsCollection elements, String separator, String... values) {
        elements.shouldHaveSize(values.length);
        for (int i = 0; i < values.length - 1; i++)
            elements.get(i).shouldHave(Condition.text(values[i] + separator));

        // если последний элемент в elements не является последним среди всех, то добавляем сепаратор
        elements.last().shouldHave(Condition.text(values[values.length - 1] +
                (element().lastChild().text().equals(elements.last().text()) ? "" : separator)));
    }
}
