package net.n2oapp.framework.autotest.api.component;

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
    void shouldNotExists();

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
}
