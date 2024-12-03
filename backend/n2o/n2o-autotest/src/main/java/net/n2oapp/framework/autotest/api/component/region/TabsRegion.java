package net.n2oapp.framework.autotest.api.component.region;

import com.codeborne.selenide.WebElementCondition;
import net.n2oapp.framework.autotest.api.component.Component;

import java.time.Duration;

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
    TabItem tab(WebElementCondition by);

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
         * @param className имя класса, по которому будет производиться поиск элементов региона.
         * @return Элемент региона (виджет/регион), соответствующий указанному классу
         */
        RegionItems content(String className);

        /**
         * Клик по вкладке
         */
        void click();

        /**
         * Проверка наименования на соответствие
         * @param text ожидаемый текст наименования
         */
        void shouldHaveName(String text, Duration... duration);

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

        /**
         * Проверка того, что вкладка доступна
         */
        void shouldBeEnabled();

        /**
         * Проверка того, что вкладка недоступна
         */
        void shouldBeDisabled();
    }
}
