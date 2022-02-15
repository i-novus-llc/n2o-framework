package net.n2oapp.framework.autotest.impl.component.header;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ImageShape;
import net.n2oapp.framework.autotest.api.component.header.MenuItem;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

/**
 * Базовый класс кнопки в меню для автотестирования
 */
public abstract class N2oMenuItem extends N2oComponent implements MenuItem {

    @Override
    public void shouldHaveImage() {
        element().$("img").shouldHave(Condition.exist);
    }

    @Override
    public void imageSrcShouldBe(String src) {
        element().$("img").shouldHave(Condition.attribute("src", src));
    }

    @Override
    public void imageShouldHaveShape(ImageShape shape) {
        switch (shape) {
            case square:
                checkShape("mr-2 n2o-nav-image ");
                break;
            case rounded:
                checkShape("mr-2 n2o-nav-image " + shape);
                break;
            case circle:
                checkShape("mr-2 n2o-nav-image rounded-" + shape);
                break;
        }
    }

    @Override
    public void labelShouldHave(String text) {
        element().shouldHave(Condition.text(text));
    }

    @Override
    public void click() {
        element().shouldBe(Condition.exist).click();
    }

    private void checkShape(String shape) {
        element().$("img").shouldHave(Condition.attribute("class", shape));
    }
}
