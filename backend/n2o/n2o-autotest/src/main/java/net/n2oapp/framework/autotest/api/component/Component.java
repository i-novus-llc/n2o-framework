package net.n2oapp.framework.autotest.api.component;

import com.codeborne.selenide.*;
import net.n2oapp.framework.api.exception.N2oException;

import java.time.Duration;

/**
 * Любой визуальный компонент для автотестирования
 */
public interface Component extends Element {

    /**
     * Проверка существования компонент на странице
     */
    void shouldExists();

    /**
     * Проверка отсутствия компонент на странице
     */
    void shouldNotExists(Duration... duration);

    /**
     * Проверка видимости компонент на странице
     */
    void shouldBeVisible();

    /**
     * Проверка скрытости компонент на странице
     */
    void shouldBeHidden();

    /**
     * Проверка наличия css класса у компонент
     *
     * @param cssClass ожидаемый css класс
     */
    void shouldHaveCssClass(String cssClass);

    default SelenideElement should(WebElementCondition condition, Duration... duration) {
        if (duration.length > 1) {
            throw new N2oException("Expected duration length 1 or less, but received %d" + duration.length);
        }
        if (duration.length == 1) {
            return element().should(condition, duration[0]);
        }

        return element().should(condition);
    }

    default SelenideElement should(WebElementCondition condition, SelenideElement element, Duration... duration) {
        if (duration.length > 1) {
            throw new N2oException("Expected duration length 1 or less, but received %d" + duration.length);
        }
        if (duration.length == 1) {
            return element.should(condition, duration[0]);
        }

        return element.should(condition);
    }

    default ElementsCollection should(WebElementsCondition condition, ElementsCollection element, Duration... duration) {
        if (duration.length == 1) {
            return element.should(condition, duration[0]);
        } else {
            return element.should(condition);
        }
    }
}
