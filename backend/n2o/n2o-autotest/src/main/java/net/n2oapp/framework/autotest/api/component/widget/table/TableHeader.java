package net.n2oapp.framework.autotest.api.component.widget.table;

import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Заголовки столбцов таблицы для автотестирования
 */
public interface TableHeader extends Component {

    /**
     * Проверка текста заголовка на соответствие
     * @param title ожидаемый текст
     */
    void shouldHaveTitle(String title);

    /**
     * Проверка отсутствия заголовка
     */
    void shouldNotHaveTitle();

    /**
     * Клик по заголовку
     */
    void click();

    /**
     * Проверка того, что заголовок в состоянии не отсортированности столбца
     */
    void shouldNotBeSorted();

    /**
     * Проверка того, что заголовок в состоянии отсортированности столбца в возрастающем порядке
     */
    void shouldBeSortedByAsc();

    /**
     * Проверка того, что заголовок в состоянии отсортированности столбца в убывающем порядке
     */
    void shouldBeSortedByDesc();

    /**
     * Проверка стиля заголовка
     * @param style ожидаемый стиль
     */
    void shouldHaveStyle(String style);

    /**
     * Проверка иконки заголовка
     * @param icon ожидаемая иконка
     */
    void shouldHaveIcon(String icon);

    /**
     * Проверка отсутствия иконки
     */
    void shouldNotHaveIcon();
}
