package net.n2oapp.framework.autotest.api.component.region;

import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Регион с автоматически прокручиваемым меню
 */
public interface ScrollspyRegion extends Region {

    /**
     * Возвращает содержание (Виджеты/Регионы) страницы по номеру
     * @param index номер необходимого блока
     * @return Компонент содержащий содержание страницы для автотестирования
     */
    ContentItem contentItem(int index);

    /**
     * Возвращает содержание (Виджеты/Регионы) страницы по заголовку
     * @param title заголовок необходимого блока
     * @return Компонент содержащий содержание страницы для автотестирования
     */
    ContentItem contentItem(String title);

    /**
     * Проверка соответствия заголовка у активного элемента
     * @param title ожидаемый заголовок
     */
    void shouldHaveActiveContentItem(String title);

    /**
     * Проверка соответствия заголовка у активного элемента меню
     * @param title ожидаемый заголовок
     */
    void shouldHaveActiveMenuItem(String title);

    /**
     * Проверка соответствия позиции элемента меню
     * @param position ожидаемый заголовок
     */
    void shouldHaveMenuPosition(MenuPosition position);

    /**
     * @return возвращает компонент меню для автотестирования
     */
    Menu menu();

    /**
     *
     */
    interface ContentItem extends Region {

        /**
         * @return Элемент региона (виджет/регион) для автотестирования
         */
        RegionItems content();
    }

    interface Menu extends Component {

        /**
         * Проверка заголовка меню на соответствие
         * @param title ожидаемый заголовок меню
         */
        void shouldHaveTitle(String title);

        /**
         * Возвращает элемент меню для автотестирования по номеру
         * @param index номер возвращаемого элемента
         * @return элемент меню для автотестирования
         */
        MenuItem menuItem(int index);

        /**
         * Возвращает элемент меню в регионе для автотестирования по заголовку
         * @param title номер возвращаемого элемента
         * @return элемент меню для автотестирования
         */
        MenuItem menuItem(String title);

        /**
         * Возвращает элементы меню из выпадающего списка по номеру
         * @param index номер возвращаемого элемента
         * @return элементы меню выпадающего списка для автотестирования
         */
        DropdownMenuItem dropdownMenuItem(int index);

        /**
         * Возвращает элементы меню из выпадающего списка по метке
         * @param label метка возвращаемого элемента
         * @return элементы меню выпадающего списка для автотестирования
         */
        DropdownMenuItem dropdownMenuItem(String label);
    }

    interface MenuItem extends Component {

        /**
         * Проверка текста
         * @param text ожидаемый текст
         */
        void shouldHaveText(String text);

        /**
         * Клик по элементу
         */
        void click();
    }

    interface DropdownMenuItem extends MenuItem {

        /**
         * Возвращает элемент меню для автотестирования по номеру
         * @param index номер возвращаемого элемента
         * @return элемент меню для автотестирования
         */
        MenuItem menuItem(int index);

        /**
         * Возвращает элемент меню в регионе для автотестирования по заголовку
         * @param title номер возвращаемого элемента
         * @return элемент меню для автотестирования
         */
        MenuItem menuItem(String title);
    }

    enum MenuPosition {
        left,
        right
    }
}
