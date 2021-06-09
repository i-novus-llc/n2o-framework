package net.n2oapp.framework.autotest.api.component.widget.table;

import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Заголовки столбцов таблицы для автотестирования
 */
public interface TableHeader extends Component {
    void shouldHaveTitle(String title);

    void shouldNotHaveTitle();

    void click();

    void shouldNotBeSorted();

    void shouldBeSortedByAsc();

    void shouldBeSortedByDesc();

    void shouldHaveStyle(String style);

    void shouldHaveCssClass(String cssClass);

    void shouldBeHidden();

    void shouldBeVisible();
}
