package net.n2oapp.framework.autotest.api.component.fieldset;

import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.Expandable;

/**
 * Филдсет с горизонтальным делителем для автотестирования
 */
public interface LineFieldSet extends FieldSet, Expandable {
    /**
     * Возвращает поля внутри филдсета
     * @return поля формы для автотестирования
     */
    Fields fields();

    /**
     * Проверка возможности раскрытия/скрытия филдсета
     */
    void shouldBeCollapsible();

    /**
     * Проверка не возможности раскрытия/скрытия филдсета
     */
    void shouldNotBeCollapsible();
}
