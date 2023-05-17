package net.n2oapp.framework.autotest.impl.component.header;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;
import net.n2oapp.framework.autotest.api.component.header.MenuItem;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

/**
 * Базовый класс кнопки в меню для автотестирования
 */
public abstract class N2oMenuItem extends N2oComponent implements MenuItem {

    @Override
    public void shouldHaveImage() {
        image().shouldHave(Condition.exist);
    }

    @Override
    public void imageShouldHaveSrc(String src) {
        image().shouldHave(Condition.attribute("src", src));
    }

    @Override
    public void imageShouldHaveShape(ShapeType shape) {
        switch (shape) {
            case SQUARE:
                checkShape("mr-2 n2o-nav-image ");
                break;
            case ROUNDED:
                checkShape(String.format("mr-2 n2o-nav-image %s", shape.getId()));
                break;
            case CIRCLE:
                checkShape(String.format("mr-2 n2o-nav-image rounded-%s", shape.getId()));
                break;
        }
    }

    @Override
    public void shouldHaveLabel(String text) {
        element().shouldHave(Condition.text(text));
    }

    @Override
    public void click() {
        element().shouldBe(Condition.exist).click();
    }

    private void checkShape(String shape) {
        image().shouldHave(Condition.attribute("class", shape));
    }

    protected SelenideElement image() {
        return element().$("img");
    }
}
