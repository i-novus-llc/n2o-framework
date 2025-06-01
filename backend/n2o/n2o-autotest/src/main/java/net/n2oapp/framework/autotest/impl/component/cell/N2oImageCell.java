package net.n2oapp.framework.autotest.impl.component.cell;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeTypeEnum;
import net.n2oapp.framework.api.metadata.meta.cell.ImageStatusElementPlaceEnum;
import net.n2oapp.framework.api.metadata.meta.control.TextPositionEnum;
import net.n2oapp.framework.autotest.api.component.cell.ImageCell;

import java.time.Duration;

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
        img().parent().parent().shouldHave(Condition.attributeMatching(
                "style", String.format(".*max-width: %dpx;.*", width)));
    }

    @Override
    public void shouldHaveShape(ShapeTypeEnum shape) {
        SelenideElement imageContainer = img().parent().parent();
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
    public void shouldHaveTitle(String title, Duration... duration) {
        should(
                Condition.exactText(title),
                element().$(".n2o-image__info .n2o-image__info_label"),
                duration
        );
    }

    @Override
    public void shouldHaveDescription(String description, Duration... duration) {
        should(
                Condition.exactText(description),
                element().$(".n2o-image__info .n2o-image__info_description"),
                duration
        );
    }

    @Override
    public void shouldHaveTextPosition(TextPositionEnum textPosition) {
        element().$(".n2o-image__content")
                .shouldHave(Condition.cssClass(textPosition.getId()));
    }

    @Override
    public void shouldHaveStatus(ImageStatusElementPlaceEnum position, int index, String title, Duration... duration) {
        should(
                Condition.text(title),
                getStatus(position, index).$(".n2o-status__text"),
                duration
        );
    }

    @Override
    public void statusShouldHaveIcon(ImageStatusElementPlaceEnum position, int index, String icon) {
        if (icon != null && !icon.isEmpty()) {
            getStatus(position, index)
                    .$(String.format(".n2o-status__icon%s", icon))
                    .should(Condition.exist);
        }
    }

    protected SelenideElement getStatus(ImageStatusElementPlaceEnum position, int index) {
        return element().$$(String.format(".n2o-image-statuses .%s", position.getId())).get(index);
    }

    protected SelenideElement img() {
        return element().$("img");
    }
}
