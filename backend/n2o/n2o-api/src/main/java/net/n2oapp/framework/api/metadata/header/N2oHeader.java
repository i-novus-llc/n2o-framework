package net.n2oapp.framework.api.metadata.header;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.control.N2oComponent;
import net.n2oapp.framework.api.metadata.menu.N2oSimpleMenu;

/**
 * Исходная модель заголовка приложения
 */
@Getter
@Setter
public class N2oHeader extends N2oComponent {
    /**
     * Видимость
     */
    private Boolean visible;

    /**
     * Основное меню
     */
    private N2oSimpleMenu menu;

    /**
     * Дополнительное меню, отображается в правой части заголовка
     */
    private N2oSimpleMenu extraMenu;

    /**
     * URL домашней страницы(переход на нее происходит по клику на логотип или название в заголовке)
     */
    private String homePageUrl;

    /**
     * Название в заголовке
     */
    private String title;

    /**
     * Путь к файлу с логотипом, который будет отображаться в заголовке
     */
    private String logoSrc;

    /**
     * Иконка открытия боковой панели, если не задана, значит кнопки не будет
     */
    private String sidebarIcon;

    /**
     * Иконка скрытия боковой панели, если не задана, значит кнопки не будет
     */
    private String sidebarToggledIcon;

    /**
     * Поиск в заголовке
     */
    private N2oSearchBar searchBar;

}
