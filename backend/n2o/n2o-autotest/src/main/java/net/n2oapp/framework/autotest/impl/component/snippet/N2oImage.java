package net.n2oapp.framework.autotest.impl.component.snippet;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;
import net.n2oapp.framework.api.metadata.meta.control.TextPosition;
import net.n2oapp.framework.autotest.api.component.snippet.Image;

/**
 * Компонент вывода изображения для автотестирования
 */
public class N2oImage extends N2oSnippet implements Image {
    @Override
    public void shouldHaveTitle(String text) {
        imageInfo().$(".n2o-image__info_label").shouldHave(Condition.text(text));
    }

    @Override
    public void shouldHaveDescription(String text) {
        imageInfo().$(".n2o-image__info_description").shouldHave(Condition.text(text));
    }

    @Override
    public void shouldHaveShape(ShapeType shape) {
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
    public void shouldHaveTextPosition(TextPosition position) {
        element().$(".n2o-image__content").should(Condition.cssClass(position.name()));
    }

    @Override
    public void shouldHaveText(String text) {
        element().shouldHave(Condition.text(text));
    }

    protected SelenideElement imageInfo() {
        return element().parent().parent().$(".n2o-image__info");
    }

    protected SelenideElement image() {
        return element().$(".n2o-image__image");
    }
}
