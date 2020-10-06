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
        img().shouldHave(Condition.attributeMatching("style", ".*max-width: "  + width + "px.*"));
    }

    @Override
    public void shapeShouldBe(ImageShape shape) {
        switch (shape) {
            case circle:
                img().parent().shouldHave(circleShapeCondition());
                break;
            case rounded:
                img().shouldHave(roundedShapeCondition());
                break;
            case polaroid:
                img().parent().shouldNotHave(circleShapeCondition());
                img().shouldNotHave(roundedShapeCondition());
                break;
        }
    }

    private Condition circleShapeCondition() {
        return Condition.attributeMatching("style", ".*clip-path: circle.*");
    }

    private Condition roundedShapeCondition() {
        return Condition.cssClass("rounded");
    }

    private SelenideElement img() {
        return element().$("img");
    }
}
