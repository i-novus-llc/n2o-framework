package net.n2oapp.framework.autotest.api.component;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.component.badge.Badge;

/**
 * Выпадающий список для автотестирования
 */
public interface DropDown extends Component {

    /**
     * Возвращает элемент из выпадающего списка по номеру
     * @param index номер элемента
     * @return Элемент выпадающего списка для автотестирования
     */
    DropDownItem item(int index);

    /**
     * Возвращает элемент из выпадающего списка по метке
     * @param label метка элемента
     * @return Элемент выпадающего списка для автотестирования
     */
    DropDownItem item(String label);

    /**
     * Проверка наличие всех элементов в выпадающего списка по метке
     * @param options список меток элементов
     */
    void shouldHaveOptions(String... options);

    /**
     * Выбор элемента из выпадающего списка по номеру
     * @param index номер элемента
     */
    void selectItem(int index);

    /**
     * Выбор элемента из выпадающего списка по условию
     * @param by условие выбора
     */
    void selectItemBy(Condition by);

    /**
     * Множественный выбор элементов из выпадающего списка по номерам
     * @param indexes массив номеров
     */
    void selectMulti(int... indexes);

    /**
     * Множественная проверка выбранности элементов из выпадающего списка
     * @param indexes массив номеров
     */
    void shouldBeChecked(int... indexes);

    /**
     * Множественная проверка невыбранности элементов из выпадающего списка
     * @param indexes массив номеров
     */
    void shouldNotBeChecked(int... indexes);

    /**
     * Проверка количества элементов в выпадающем списке
     * @param size ожидаемое количество
     */
    void shouldHaveOptions(int size);

    /**
     * Элемент выпадающего списка для автотестирования
     */
    interface DropDownItem extends Component, Badge {

        /**
         * Проверка текста
         * @param value ожидаемый текст
         */
        void shouldHaveValue(String value);

        /**
         * Проверка выбранности
         */
        void shouldBeSelected();

        /**
         * Проверка невыбранности
         */
        void shouldNotBeSelected();

        /**
         * Проверка соответствия описания
         * @param description ожидаемое описание
         */
        void shouldHaveDescription(String description);

        /**
         * Проверка соответствия цвета
         * @param color ожидаемый цвет
         */
        void shouldHaveStatusColor(Colors color);

        /**
         * Проверка доступности для выбора
         */
        void shouldBeEnabled();

        /**
         * Проверка недоступности для выбора
         */
        void shouldBeDisabled();
    }
}
