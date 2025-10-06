package net.n2oapp.framework.api.metadata.header;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.DatasourceIdAware;
import net.n2oapp.framework.api.metadata.control.N2oComponent;
import net.n2oapp.framework.api.metadata.menu.N2oSimpleMenu;

/**
 * Исходная модель шапки приложения
 */
@Getter
@Setter
public class N2oHeader extends N2oComponent implements DatasourceIdAware {
    /**
     * Заголовок шапки
     */
    private String title;
    /**
     * Идентификатор источника данных
     */
    private String datasourceId;
    /**
     * URL домашней страницы. Переход на нее происходит по клику на логотип или заголовок
     */
    private String homePageUrl;
    /**
     * Путь к файлу с логотипом, который будет отображаться в шапке
     */
    private String logoSrc;
    /**
     * Иконка открытия боковой панели
     */
    private String sidebarIcon;
    /**
     * Иконка скрытия боковой панели
     */
    private String sidebarToggledIcon;

    /**
     * Основное меню
     */
    private N2oSimpleMenu navMenu;
    /**
     * Дополнительное меню. Отображается в правой части заголовка
     */
    private N2oSimpleMenu extraMenu;
    /**
     * Панель поиска
     */
    private N2oSearchBar searchBar;
}
