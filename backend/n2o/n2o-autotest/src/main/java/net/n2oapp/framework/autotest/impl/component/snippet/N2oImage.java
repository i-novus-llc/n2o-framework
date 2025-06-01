package net.n2oapp.framework.autotest.impl.component.snippet;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeTypeEnum;
import net.n2oapp.framework.api.metadata.meta.control.TextPositionEnum;
import net.n2oapp.framework.autotest.api.component.snippet.Image;

import java.time.Duration;

/**
 * Компонент вывода изображения для автотестирования
 */
public class N2oImage extends N2oSnippet implements Image {
    @Override
    public void shouldHaveTitle(String text, Duration... duration) {
        should(Condition.text(text), imageInfo().$(".n2o-image__info_label"), duration);
    }

    @Override
    public void shouldHaveDescription(String text, Duration... duration) {
        should(Condition.text(text), imageInfo().$(".n2o-image__info_description"), duration);
    }

    @Override
    public void shouldHaveShape(ShapeTypeEnum shape) {
        element().$(".n2o-image__image-container").should(Condition.cssClass(shape.getId()));
    }

    @Override
    public void shouldHaveUrl(String url) {
        image().should(Condition.attribute("src", url));
    }

    @Override
    public void shouldHaveWidth(int width) {
        image().should(
                Condition.attributeMatching("style", String.format(".*width: %dpx;.*", width)));
    }

    @Override
    public void shouldHaveTextPosition(TextPositionEnum position) {
        element().$(".n2o-image__content").should(Condition.cssClass(position.getId()));
    }

    @Override
    public void shouldHaveText(String text, Duration... duration) {
        should(Condition.text(text), duration);
    }

    protected SelenideElement imageInfo() {
        return element().parent().parent().$(".n2o-image__info");
    }

    protected SelenideElement image() {
        return element().$(".n2o-image__image");
    }
}
