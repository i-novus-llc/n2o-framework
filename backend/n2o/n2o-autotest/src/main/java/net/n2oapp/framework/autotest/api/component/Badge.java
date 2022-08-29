package net.n2oapp.framework.autotest.api.component;

import com.codeborne.selenide.Condition;

public interface Badge extends Component{
    default void badgeShouldBeExists() {
        element().$(".n2o-badge").shouldBe(Condition.exist);
    }
    default void badgeShouldHaveImage() {
        element().$(".n2o-badge").$(".n2o-badge-image").shouldBe(Condition.exist);
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
}
