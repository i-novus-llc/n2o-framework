package net.n2oapp.framework.autotest.api.component.badge;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.Component;

public interface Badge extends Component {
    default void badgeShouldBeExists() {
        element().$(".n2o-badge").shouldBe(Condition.exist);
    }

    default void badgeShouldHaveImage(String imageSrc) {
        element().$(".n2o-badge").$(".n2o-badge-image").shouldBe(Condition.exist);
        element().$(".n2o-badge").$(".n2o-badge-image").shouldBe(Condition.attributeMatching("src", ".*" + imageSrc));
    }

    default void badgeShouldHaveShape(String shape) {
        element().$(".n2o-badge").shouldHave(Condition.cssClass(String.format("n2o-badge--%s", shape)));
    }

    default void badgeImageShouldBeShape(String shape) {
        element().$(".n2o-badge").$(".n2o-badge-image").shouldHave(Condition.cssClass(String.format("n2o-badge-image--%s", shape)));
    }

    default void badgeImageShouldBePosition(String position) {
        element().$(".n2o-badge").$(".n2o-badge-image").shouldHave(Condition.cssClass(String.format("n2o-badge-image--%s", position)));
    }

    default void badgeShouldHaveText(String text) {
        element().$(".n2o-badge").shouldHave(Condition.text(text));
    }
}
