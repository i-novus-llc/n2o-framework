package net.n2oapp.framework.autotest.api.component.badge;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.BadgePosition;
import net.n2oapp.framework.autotest.BadgeShape;
import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.component.Component;

import java.time.Duration;

/**
 * Ячейка со значком для автотестирования
 */
public interface Badge extends Component {
    /**
     * Проверка наличия баджа внутри элемента
     */
    default void badgeShouldExists() {
        element().$(".n2o-badge").shouldBe(Condition.exist);
    }

    /**
     * Проверка отсутствия баджа внутри элемента
     */
    default void badgeShouldNotExists() {
        element().$(".n2o-badge").shouldNotBe(Condition.visible);
    }

    /**
     * Проверка наличие картинки и ее соответствие ожидаемому значению
     * @param imageSrc ожидаемый путь к картинке
     */
    default void badgeShouldHaveImage(String imageSrc) {
        element().$(".n2o-badge")
                .$(".n2o-badge-image")
                .shouldBe(Condition.exist, Condition.attributeMatching("src", ".*" + imageSrc));
    }

    /**
     * Проверка баджа на соответствие ожидаемой форме
     * @param shape ожидаемая форма баджа
     */
    default void badgeShouldHaveShape(BadgeShape shape) {
        element().$(".n2o-badge")
                .shouldHave(Condition.cssClass(shape.name("n2o-badge--")));
    }

    /**
     * Проверка позиции баджа на соответствие ожидаемой позиции
     * @param position ожидаемая позиция баджа
     */
    default void badgeShouldHavePosition(BadgePosition position) {
        if (position.equals(BadgePosition.LEFT))
            element().$$("span").get(0).shouldHave(Condition.cssClass("badge"));
        else
            element().$$("span").get(1).shouldHave(Condition.cssClass("badge"));
    }

    /**
     * Проверка картинки внутри баджа на соответствие ожидаемой форме
     * @param shape ожидаемая форма картинки
     */
    default void badgeShouldHaveImageShape(BadgeShape shape) {
        element().$(".n2o-badge")
                .$(".n2o-badge-image")
                .shouldHave(Condition.cssClass(shape.name("n2o-badge-image--")));
    }

    /**
     * Проверка позиции картинки внутри баджа на соответствие ожидаемой позиции
     * @param position ожидаемая позиция картинки
     */
    default void badgeShouldHaveImagePosition(BadgePosition position) {
        element().$(".n2o-badge")
                .$(".n2o-badge-image")
                .shouldHave(Condition.cssClass(position.name("n2o-badge-image--")));
    }

    /**
     * Проверка текста внутри баджа на точное соответствие (не учитывая регистр) ожидаемому тексту
     * @param text ожидаемый текст баджа
     */
    default void badgeShouldHaveText(String text, Duration... duration) {
        should(Condition.exactText(text), element().$(".n2o-badge"), duration);
    }

    /**
     * Проверка на отсутствие текста внутри баджа
     */
    default void badgeShouldNotHaveText(Duration... duration) {
        should(Condition.empty, element().$(".n2o-badge"), duration);
    }

    /**
     * Проверка цвета на соответствие
     * @param color ожидаемый цвет баджа
     */
    default void badgeShouldHaveColor(Colors color){
        element().$(".n2o-badge")
                .shouldHave(Condition.cssClass(String.format("badge-%s", color.name().toLowerCase())));
    }

}