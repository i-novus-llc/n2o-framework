package net.n2oapp.framework.autotest.api.component.region;

import net.n2oapp.framework.autotest.api.component.Component;

import java.time.Duration;

/**
 * Регион с автоматически прокручиваемым меню
 */
public interface ScrollspyRegion extends Region {

    /**
     * Возвращает содержание (Виджеты/Регионы) страницы по номеру
     *
     * @param index номер необходимого блока
     * @return Компонент содержащий содержание страницы для автотестирования
     */
    ContentItem contentItem(int index);

    /**
     * Возвращает содержание (Виджеты/Регионы) страницы по заголовку
     *
     * @param title заголовок необходимого блока
     * @return Компонент содержащий содержание страницы для автотестирования
     */
    ContentItem contentItem(String title);

    /**
     * Проверка соответствия заголовка у активного элемента
     *
     * @param title ожидаемый заголовок
     */
    void activeContentItemShouldHaveTitle(String title, Duration... duration);

    /**
     * Проверка соответствия заголовка у активного элемента меню
     *
     * @param title ожидаемый заголовок
     */
    void activeMenuItemShouldHaveTitle(String title, Duration... duration);

    /**
     * Проверка соответствия позиции элемента меню
     *
     * @param position ожидаемый заголовок
     */
    void menuShouldHavePosition(MenuPositionEnum position);

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

        /**
         * @param className имя класса, по которому будет производиться поиск элементов региона.
         * @return Элемент региона (виджет/регион), соответствующий указанному классу
         */
        RegionItems content(String className);

        /**
         * Низ региона будет выровнен по низу
         */
        void scrollDown();

        /**
         * Верх региона будет выровнен по верху
         */
        void scrollUp();
    }

    interface Menu extends Component {

        /**
         * Проверка заголовка меню на соответствие
         *
         * @param title ожидаемый заголовок меню
         */
        void shouldHaveTitle(String title, Duration... duration);

        /**
         * Возвращает элемент меню для автотестирования по номеру
         *
         * @param index номер возвращаемого элемента
         * @return элемент меню для автотестирования
         */
        MenuItem menuItem(int index);

        /**
         * Возвращает элемент меню в регионе для автотестирования по заголовку
         *
         * @param title номер возвращаемого элемента
         * @return элемент меню для автотестирования
         */
        MenuItem menuItem(String title);

        /**
         * Возвращает элементы меню из выпадающего списка по номеру
         *
         * @param index номер возвращаемого элемента
         * @return элементы меню выпадающего списка для автотестирования
         */
        DropdownMenuItem dropdownMenuItem(int index);

        /**
         * Возвращает элементы меню из выпадающего списка по метке
         *
         * @param label метка возвращаемого элемента
         * @return элементы меню выпадающего списка для автотестирования
         */
        DropdownMenuItem dropdownMenuItem(String label);

        /**
         * Возвращает элементы меню из групп по номеру
         *
         * @param index номер возвращаемого элемента
         * @return элементы меню выпадающего списка для автотестирования
         */
        GroupItem group(int index);

        /**
         * Возвращает элементы меню из групп по метке
         *
         * @param label метка возвращаемого элемента
         * @return элементы меню выпадающего списка для автотестирования
         */
        GroupItem group(String label);
    }

    interface MenuItem extends Component {

        /**
         * Проверка текста
         *
         * @param text ожидаемый текст
         */
        void shouldHaveText(String text, Duration... duration);

        /**
         * Клик по элементу
         */
        void click();
    }

    interface DropdownMenuItem extends MenuItem {

        /**
         * Возвращает элемент меню для автотестирования по номеру
         *
         * @param index номер возвращаемого элемента
         * @return элемент меню для автотестирования
         */
        MenuItem menuItem(int index);

        /**
         * Возвращает элемент меню в регионе для автотестирования по заголовку
         *
         * @param title номер возвращаемого элемента
         * @return элемент меню для автотестирования
         */
        MenuItem menuItem(String title);

        /**
         * Проверка раскрыто ли выпадающее меню
         */
        void shouldBeExpand();

        /**
         * Проверка закрыто ли выпадающее меню
         */
        void shouldBeCollapse();
    }

    interface GroupItem extends Menu {

        /**
         * Проверка наличия разделительной линии
         */
        void shouldHaveHeadline();

        /**
         * Проверка отсутствия разделительной линии
         */
        void shouldNotHaveHeadline();
    }

    enum MenuPositionEnum {
        LEFT,
        RIGHT
    }
}
