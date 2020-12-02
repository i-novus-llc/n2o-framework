package net.n2oapp.framework.autotest.impl.component.snippet;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.api.metadata.meta.control.TextPosition;
import net.n2oapp.framework.autotest.api.component.snippet.Image;

/**
 * Компонент вывода изображения для автотестирования
 */
public class N2oImage extends N2oSnippet implements Image {
    @Override
    public void shouldHaveTitle(String text) {
        element().$(".n2o-image__content .n2o-image__info .n2o-image__info_label").shouldHave(Condition.text(text));
    }

    @Override
    public void shouldHaveDescription(String text) {
        element().$(".n2o-image__content .n2o-image__info .n2o-image__info_description").shouldHave(Condition.text(text));
    }

    @Override
    public void shouldHaveUrl(String url) {
        element().$(".n2o-image__image").should(Condition.attribute("src", url));
    }

    @Override
    public void shouldHaveWidth(int size) {
        element().$(".n2o-image__image-container").should(
                Condition.attributeMatching("style", ".*width: " + size + "px; height: " + size + "px;.*"));
    }

    @Override
    public void shouldHaveTextPosition(TextPosition position) {
        element().$(".n2o-image__content").should(Condition.cssClass(position.name()));
    }

    @Override
    public void shouldHaveText(String text) {
        element().shouldHave(Condition.text(text));
    }
}
