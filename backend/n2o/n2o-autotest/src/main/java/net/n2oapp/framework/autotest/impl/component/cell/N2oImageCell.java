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
                "style", ".*max-width: " + width + "px;.*"));
    }

    @Override
    public void shouldHaveShape(ShapeType shape) {
        switch (shape) {
            case CIRCLE:
                imgShouldHaveCircleShape();
                break;
            case ROUNDED:
                imgShouldHaveRoundedShape();
                break;
            case SQUARE:
                imgShouldHaveSquareShape();
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
                .shouldBe(Condition.cssClass(textPosition.name()));
    }

    @Override
    public void shouldHaveStatus(ImageStatusElementPlace position, int index, String title) {
        getStatus(position, index)
                .$(".n2o-status__text")
                .shouldBe(Condition.text(title));
    }

    private SelenideElement getStatus(ImageStatusElementPlace position, int index) {
        return element().$$(".n2o-image-statuses ." + position).get(index);
    }

    @Override
    public void shouldHaveStatusIcon(ImageStatusElementPlace position, int index, String icon) {
        if (icon != null && !icon.isEmpty()) {
            element().$(".n2o-image-statuses ." + position)
                    .$(".n2o-status__icon"+icon)
                    .should(Condition.exist);
        }
    }

    private void imgShouldHaveCircleShape() {
        img().parent().shouldHave(Condition.cssClass("circle"));
    }

    private void imgShouldHaveRoundedShape() {
        img().parent().shouldHave(Condition.cssClass("rounded"));
    }

    private void imgShouldHaveSquareShape() {
        //ToDo: почему проверка на rounded внутри circle?
        img().parent().shouldNotHave(Condition.cssClass("circle"));
        img().parent().shouldNotHave(Condition.cssClass("rounded"));
    }

    private SelenideElement img() {
        return element().$("img");
    }
}
