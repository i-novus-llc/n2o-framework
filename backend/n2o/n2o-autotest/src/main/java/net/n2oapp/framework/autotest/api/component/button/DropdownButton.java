package net.n2oapp.framework.autotest.api.component.button;

import com.codeborne.selenide.Condition;

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
}
