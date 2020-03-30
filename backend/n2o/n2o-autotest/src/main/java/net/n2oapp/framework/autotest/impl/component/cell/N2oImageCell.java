package net.n2oapp.framework.autotest.impl.component.cell;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ImageShape;
import net.n2oapp.framework.autotest.api.component.cell.ImageCell;

/**
 * Ячейка таблицы с картинкой для автотестирования
 */
public class N2oImageCell extends N2oCell implements ImageCell {

    @Override
    public void srcShouldBe(String src) {
        element().$("img").shouldBe(Condition.attribute("src", src));
    }

    @Override
    public void imageShouldBe(String url) {
        element().$("img").shouldHave(Condition.attribute("src", url));
    }

    @Override
    public void shapeShouldBe(ImageShape shape) {
        switch (shape) {
            case circle:
                element().$("img").shouldHave(Condition.cssClass("rounded-circle"));
                break;
            case rounded:
                element().$("img").shouldHave(Condition.cssClass("rounded"));
                break;
            case polaroid:
                element().$("img").shouldNotHave(Condition.cssClass("rounded"),Condition.cssClass("rounded-circle"));
                break;
        }
    }
}
