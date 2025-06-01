package net.n2oapp.framework.autotest.api.component.snippet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;
import net.n2oapp.framework.autotest.ColorsEnum;
import net.n2oapp.framework.autotest.api.component.Component;

import java.time.Duration;

/**
 * Компонент предупреждения для автотестирования
 */
public interface Alert extends Snippet {

    /**
     * Проверка цвета на соответствие
     *
     * @param color ожидаемый цвет предупреждения
     */
    void shouldHaveColor(ColorsEnum color);

    /**
     * Проверка заголовка на соответствие
     *
     * @param text ожидаемый заголовок предупреждения
     */
    void shouldHaveTitle(String text, Duration... duration);

    /**
     * Проверка ссылки на соответвие
     *
     * @param url ожидаемая ссылка
     */
    void shouldHaveUrl(String url);

    /**
     * Проверка того, что предупреждение содержит дополнительную информацию
     */
    void shouldHaveStacktrace();

    /**
     * Проверка того, что предупреждение содержит информацию о времени
     */
    void shouldHaveTimestamp(String timestamp, Duration... duration);

    /**
     * Проверка того, что предупреждение отсутствует
     *
     * @param duration задержка
     */
    void shouldNotExists(Duration... duration);

    /**
     * Клик по предупреждению
     */
    void click();

    /**
     * @return кнопка закрытия прежупреждения
     */
    CloseButton closeButton();

    /**
     * Кнопка закрытия прежупреждения
     */
    interface CloseButton extends Component {

        /**
         * Клик по кнопке
         */
        void click();
    }
    @RequiredArgsConstructor
    @Getter
    enum PlacementEnum implements N2oEnum {
        TOP("top"),
        BOTTOM("bottom"),
        TOP_LEFT("topLeft"),
        TOP_RIGHT("topRight"),
        BOTTOM_LEFT("bottomLeft"),
        BOTTOM_RIGHT("bottomRight");

        private final String id;
    }
}
