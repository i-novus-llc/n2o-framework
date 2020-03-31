package net.n2oapp.framework.autotest.api.component.widget.table;

/**
 * Стандартный заголовок столбца таблицы для автотестирования
 */
public interface StandardTableHeader extends TableHeader {
    void shouldHaveTitle(String title);

    void shouldNotHaveTitle();

    void click();

    void shouldNotBeSorted();

    void shouldBeSortedByAsc();

    void shouldBeSortedByDesc();
}
