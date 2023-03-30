package net.n2oapp.framework.autotest.api.component.badge;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.BadgePosition;
import net.n2oapp.framework.autotest.BadgeShape;
import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Ячейка со значком для автотестирования
 */
public interface Badge extends Component {
    /**
     * Проверка наличия баджа внутри элемента
     */
    default void badgeShouldBeExists() {
        element().$(".n2o-badge").shouldBe(Condition.exist);
    }

    /**
     * Проверка отсутствия баджа внутри элемента
     */
    default void badgeShouldNotBeExists() {
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
    default void badgeShouldHaveText(String text) {
        element().$(".n2o-badge").shouldHave(Condition.exactText(text));
    }

    /**
     * Проверка на отсутствие текста внутри баджа
     */
    default void badgeShouldNotHaveText() {
        element().$(".n2o-badge").shouldHave(Condition.empty);
    }
}
