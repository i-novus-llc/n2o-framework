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
    default void shouldBeExists() {
        element().$(".n2o-badge").shouldBe(Condition.exist);
    }

    /**
     * Проверка наличие картинки и ее соответствие ожидаемому значению
     * @param regex регулярное выражение соответствующее ожидаемому путю к картинке
     */
    default void shouldHaveImageMatches(String regex) {
        element().$(".n2o-badge")
                .$(".n2o-badge-image")
                .shouldBe(Condition.exist, Condition.attributeMatching("src", regex));
    }

    /**
     * Проверка баджа на соответствие ожидаемой форме
     * @param shape ожидаемая форма баджа
     */
    default void shouldHaveShape(BadgeShape shape) {
        element().$(".n2o-badge")
                .shouldHave(Condition.cssClass(shape.name("n2o-badge--")));
    }

    /**
     * Проверка картинки внутри баджа на соответствие ожидаемой форме
     * @param shape ожидаемая форма картинки
     */
    default void shouldHaveImageShape(BadgeShape shape) {
        element().$(".n2o-badge")
                .$(".n2o-badge-image")
                .shouldHave(Condition.cssClass(shape.name("n2o-badge-image--")));
    }

    /**
     * Проверка позиции картинки внутри баджа на соответствие ожидаемой позиции
     * @param position ожидаемая позиция картинки
     */
    default void shouldHaveImagePosition(BadgePosition position) {
        element().$(".n2o-badge")
                .$(".n2o-badge-image")
                .shouldHave(Condition.cssClass(position.name("n2o-badge-image--")));
    }

    /**
     * Проверка текста внутри баджа на точное соответствие (не учитывая регистр) ожидаемому тексту
     * @param text ожидаемый текст баджа
     */
    default void shouldHaveText(String text) {
        element().$(".n2o-badge").shouldHave(Condition.exactText(text));
    }
}
