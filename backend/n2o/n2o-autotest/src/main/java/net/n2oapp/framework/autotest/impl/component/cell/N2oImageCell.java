package net.n2oapp.framework.autotest.impl.component.cell;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ImageShape;
import net.n2oapp.framework.autotest.TextPosition;
import net.n2oapp.framework.autotest.api.component.cell.ImageCell;

/**
 * Ячейка таблицы с изображением для автотестирования
 */
public class N2oImageCell extends N2oCell implements ImageCell {

    @Override
    public void srcShouldBe(String src) {
        img().shouldBe(Condition.attribute("src", src));
    }

    @Override
    public void imageShouldBe(String url) {
        img().shouldHave(Condition.attribute("src", url));
    }

    @Override
    public void widthShouldBe(int width) {
        img().parent().shouldHave(Condition.attributeMatching(
                "style", ".*width: " + width + "px; height: " + width + "px.*"));
    }

    @Override
    public void shapeShouldBe(ImageShape shape) {
        switch (shape) {
            case circle:
                imgShouldHaveCircleShape();
                break;
            case rounded:
                imgShouldHaveRoundedShape();
                break;
            case square:
                imgShouldHaveSquareShape();
                break;
        }
    }

    @Override
    public void shouldHaveTitle(String title) {
        element().$(".n2o-image__info .n2o-image__info_label").shouldBe(Condition.text(title));
    }

    @Override
    public void shouldHaveDescription(String description) {
        element().$(".n2o-image__info .n2o-image__info_description").shouldBe(Condition.text(description));
    }

    @Override
    public void shouldHaveTextPosition(TextPosition textPosition) {
        element().$(".n2o-image__content").shouldBe(Condition.cssClass(textPosition.toString().toLowerCase()));
    }

    private void imgShouldHaveCircleShape() {
        img().parent().shouldHave(Condition.cssClass("circle"));
    }

    private void imgShouldHaveRoundedShape() {
        img().parent().shouldHave(Condition.cssClass("rounded"));
    }

    private void imgShouldHaveSquareShape() {
        img().parent().shouldNotHave(Condition.cssClass("circle"));
        img().parent().shouldNotHave(Condition.cssClass("rounded"));
    }

    private SelenideElement img() {
        return element().$("img");
    }
}
