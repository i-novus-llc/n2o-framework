package net.n2oapp.framework.autotest.api.component;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

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
     * @param cssClass ожидаемый css класс
     */
    void shouldHaveCssClass(String cssClass);

    default SelenideElement should(Condition condition, Duration... duration) {
        if (duration.length == 1) {
            return element().should(condition, duration[0]);
        } else {
            return element().should(condition);
        }
    }

}
