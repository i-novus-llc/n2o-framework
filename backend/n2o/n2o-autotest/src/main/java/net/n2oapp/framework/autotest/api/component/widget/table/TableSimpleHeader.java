package net.n2oapp.framework.autotest.api.component.widget.table;

/**
 * Заголовок простого столбца таблицы для автотестирования
 */
public interface TableSimpleHeader extends TableHeader {

    /**
     * Проверка доступности функции drag-n-drop
     */
    void shouldBeDraggable();

    /**
     * Проверка недоступности функции drag-n-drop
     */
    void shouldNotBeDraggable();

    /**
     * Проверка наличия иконки drag-n-drop для перемещения
     */
    void shouldHaveDndIcon();

    /**
     * Проверка отсутствия иконки drag-n-drop для перемещения
     */
    void shouldNotHaveDndIcon();

    /**
     * Выполняет действия drag-and-drop для перемещения заголовка таблицы на указанное целевое место.
     *
     * @param header целевой заголовок таблицы, на который будет выполнено перетаскивание
     */
    void dragAndDropTo(TableSimpleHeader header);
}
