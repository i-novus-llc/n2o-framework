package net.n2oapp.framework.autotest.impl.component.cell;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;
import net.n2oapp.framework.api.metadata.meta.cell.ImageStatusElementPlace;
import net.n2oapp.framework.api.metadata.meta.control.TextPosition;
import net.n2oapp.framework.autotest.api.component.cell.ImageCell;

/**
 * Ячейка таблицы с изображением для автотестирования
 */
public class N2oImageCell extends N2oCell implements ImageCell {

    @Override
    public void shouldHaveSrc(String src) {
        img().shouldHave(Condition.attribute("src", src));
    }

    @Override
    public void shouldHaveWidth(int width) {
        img().parent().shouldHave(Condition.attributeMatching(
                "style", String.format(".*max-width: %dpx;.*", width)));
    }

    @Override
    public void shouldHaveShape(ShapeType shape) {
        SelenideElement imageContainer = img().parent();
        switch (shape) {
            case CIRCLE:
                imageContainer.shouldHave(Condition.cssClass("circle"));
                break;
            case ROUNDED:
                imageContainer.shouldHave(Condition.cssClass("rounded"));
                break;
            case SQUARE:
                imageContainer.shouldNotHave(Condition.cssClass("circle"), Condition.cssClass("rounded"));
                break;
        }
    }

    @Override
    public void shouldHaveTitle(String title) {
        element().$(".n2o-image__info .n2o-image__info_label")
                .shouldBe(Condition.exactText(title));
    }

    @Override
    public void shouldHaveDescription(String description) {
        element().$(".n2o-image__info .n2o-image__info_description")
                .shouldBe(Condition.exactText(description));
    }

    @Override
    public void shouldHaveTextPosition(TextPosition textPosition) {
        element().$(".n2o-image__content")
                .shouldHave(Condition.cssClass(textPosition.name()));
    }

    @Override
    public void shouldHaveStatus(ImageStatusElementPlace position, int index, String title) {
        getStatus(position, index)
                .$(".n2o-status__text")
                .shouldBe(Condition.text(title));
    }

    @Override
    public void statusShouldHaveIcon(ImageStatusElementPlace position, int index, String icon) {
        if (icon != null && !icon.isEmpty()) {
            getStatus(position, index)
                    .$(String.format(".n2o-status__icon%s", icon))
                    .should(Condition.exist);
        }
    }

    protected SelenideElement getStatus(ImageStatusElementPlace position, int index) {
        return element().$$(String.format(".n2o-image-statuses .%s", position)).get(index);
    }

    protected SelenideElement img() {
        return element().$("img");
    }
}
