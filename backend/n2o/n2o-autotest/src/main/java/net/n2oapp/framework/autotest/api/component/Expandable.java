package net.n2oapp.framework.autotest.api.component;

/**
 * Раскрывающиеся компонент
 */
public interface Expandable {

    /**
     * Раскрытие выпадающего списка
     */
    void expand();

    /**
     * Скрытие выпадающего списка
     */
    void collapse();

    /**
     * Проверка того, что выпадающий список раскрыт
     */
    void shouldBeExpanded();

    /**
     * Проверка того, что выпадающий список скрыт
     */
    void shouldBeCollapsed();
}
