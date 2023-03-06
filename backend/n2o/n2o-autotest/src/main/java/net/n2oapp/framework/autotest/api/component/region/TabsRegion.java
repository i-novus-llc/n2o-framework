package net.n2oapp.framework.autotest.api.component.region;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Регион в виде вкладок для автотестирования
 */
public interface TabsRegion extends Region {

    /**
     * Возвращает элемент вкладку по номеру
     * @param index номер вкладки
     * @return Компонент вкладка для автотестирования
     */
    TabItem tab(int index);

    /**
     * Возвращает элемент вкладку по словию
     * @param by услвоие поиска
     * @return Компонент вкладка для автотестирования
     */
    TabItem tab(Condition by);

    /**
     * Проверка количества вкладок
     * @param size ожидаемое количество
     */
    void shouldHaveSize(int size);

    /**
     * Проверка максимальной высоты
     * @param height ожидаемая максимальная высота в пикселях
     */
    void shouldHaveMaxHeight(int height);

    /**
     * Проверка наличия полосы прокрутки
     */
    void shouldHaveScrollbar();

    /**
     * Проверка отсутствия полосы прокрутки
     */
    void shouldNotHaveScrollbar();

    /**
     * Компонент вкладка для автотестирования
     */
    interface TabItem extends Component {
        /**
         * @return Элемент региона (виджет/регион) для автотестирования
         */
        RegionItems content();

        /**
         * Клик по вкладке
         */
        void click();

        /**
         * Проверка наименования на соответствие
         * @param text ожидаемый текст наименования
         */
        void shouldHaveName(String text);

        /**
         * Проверка отсутствия наименования
         */
        void shouldNotHaveTitle();

        /**
         * Проверка того, что вкладка выбрана/открыта
         */
        void shouldBeActive();

        /**
         * Проверка того, что вкладка не выбрана/открыта
         */
        void shouldNotBeActive();

        /**
         * Проверка того, что вкладка не прошла валидацию
         */
        void shouldBeInvalid();

        /**
         * Проверка того, что вкладка прошла валидацию
         */
        void shouldBeValid();

        /**
         * Прокрутка страницы вверх
         */
        void scrollUp();

        /**
         * Прокрутка страницы вниз
         */
        void scrollDown();
    }
}
