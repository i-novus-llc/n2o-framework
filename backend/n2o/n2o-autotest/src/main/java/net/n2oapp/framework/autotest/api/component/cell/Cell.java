package net.n2oapp.framework.autotest.api.component.cell;

import net.n2oapp.framework.autotest.api.component.Component;
import net.n2oapp.framework.autotest.api.component.Tooltip;

import java.time.Duration;

/**
 * Стандатная ячейка для автотестирования
 */
public interface Cell extends Component {
    /**
     * Проверка того, что ячейка пустая
     */
    void shouldBeEmpty(Duration... duration);

    /**
     * Проверка того, что ячейка раскрываемая
     */
    void shouldBeExpandable();

    /**
     * Проверка того, что ячейка не раскрываемая
     */
    void shouldNotBeExpandable();

    /**
     * Проверка того, что ячейка раскрыта
     */
    void shouldBeExpanded();

    /**
     * Проверка того, что ячейка не раскрыта
     */
    void shouldBeCollapsed();

    /**
     * Раскрыть/скрыть ячейку
     */
    void expand();

    /**
     * Провека иконки ячейки на соответствие ожидаемому
     * @param icon ожидаемое значение иконки
     */
    void shouldHaveIcon(String icon);

    /**
     * Проверка того, что ячейка не содержит иконку
     */
    void shouldNotHaveIcon();

    /**
     * Проверка выравнивания ячейки
     * @param alignment ожидаемое выравнивание
     */
    void shouldHaveAlignment(String alignment);

    /**
     * Возвращает тултип ячейки
     * @return Тултип для автотестирования
     */
    Tooltip tooltip();

    /**
     * Наведение мыши на ячейку
     */
    void hover();
}
