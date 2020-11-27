package net.n2oapp.framework.autotest.impl.component.cell;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ImageShape;
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
            case polaroid:
                imgShouldHavePolaroidShape();
                break;
        }
    }

    private void imgShouldHaveCircleShape() {
        img().parent().shouldHave(Condition.cssClass("circle"));
    }

    private void imgShouldHaveRoundedShape() {
        img().parent().shouldHave(Condition.cssClass("rounded"));
    }

    private void imgShouldHavePolaroidShape() {
        img().parent().shouldNotHave(Condition.cssClass("circle"));
        img().parent().shouldNotHave(Condition.cssClass("rounded"));
    }

    private SelenideElement img() {
        return element().$("img");
    }
}
