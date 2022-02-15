package net.n2oapp.framework.autotest.api.component;

/**
 * Любой визуальный компонент для автотестирования
 */
public interface Component extends Element {

    void shouldExists();
    void shouldNotExists();

    void shouldBeVisible();
    void shouldBeHidden();
}
