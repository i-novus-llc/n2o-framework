package net.n2oapp.framework.autotest.api.component.button;

import com.codeborne.selenide.Condition;

import java.time.Duration;

/**
 * Кнопка с выпадающим меню для автотестирования
 */
public interface DropdownButton extends Button {
    /**
     * Провека количества кнопок
     * @param count ожидаемое количество кнопок
     */
    void shouldHaveItems(int count);

    /**
     * Проверка точного соответствия метки (без учета регистра) у кнопки
     * @param label ожидаемое значение метки
     */
    void shouldHaveLabel(String label, Duration... duration);

    /**
     * Проверка описания на соответствие
     * @param text ожидаемый текст описания
     */
    void shouldHaveDescription(String text, Duration... duration);
    /**
     * Возвращает первую стандартную кнопку, метка которой соответствует ожидаемому
     * @param label ожидаемая метка кнопки
     * @return Стандартная кнопка для автотестирования
     */
    StandardButton menuItem(String label);

    /**
     * Возвращает первую стандартную кнопку, которой соответствует ожидаемому условию
     * @param by условие поиска
     * @return Стандартная кнопка для автотестирования
     */
    StandardButton menuItem(Condition by);

    /**
     * Не поддерживаемый метод, следует использовать метод shouldBeHidden()
     */
    @Deprecated
    void shouldNotBeVisible();

    /**
     * Проверка того, что список кнопок раскрыт
     */
    void shouldBeExpanded();

    /**
     * Проверка того, что список кнопок скрыт
     */
    void shouldBeCollapsed();

    /**
     * Проверка иконки у кнопки на соответствие ожидаемому значению
     * @param icon ожидаемое имя иконки
     */
    void shouldHaveIcon(String icon);
}
