package net.n2oapp.framework.api.metadata.application;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.control.N2oComponent;
import net.n2oapp.framework.api.metadata.menu.N2oSimpleMenu;

/**
 * Боковая панель приложения
 */
@Getter
@Setter
public class N2oSidebar extends N2oComponent {

    /**
     * Видимость
     */
    private Boolean visible;

    /**
     * Сторона появления
     */
    private Side side;

    /**
     * Путь к файлу с логотипом, который будет отображаться в заголовке боковой панели
     */
    private String logoSrc;

    /**
     * Название в заголовке боковой панели
     */
    private String title;

    /**
     * URL страницы, переход на которую происходит по клику на логотип или название
     */
    private String homePageUrl;

    /**
     * css класс для области с логотипом и названием
     */
    private String logoClass;

    /**
     * Состояние сайдбара по умолчанию
     */
    private SidebarState defaultState;

    /**
     * Состояние боковой панели принимаемое при нажатии на кнопку toggle-sidebar
     */
    private SidebarState toggledState;

    /**
     * Открывается ли панель по hover
     */
    private Boolean toggleOnHover;

    /**
     * Перекрывает ли боковая панель контент страницы
     */
    private Boolean overlay;

    /**
     * Основное меню
     */
    private N2oSimpleMenu menu;

    /**
     * Дополнительное меню
     */
    private N2oSimpleMenu extraMenu;
}
