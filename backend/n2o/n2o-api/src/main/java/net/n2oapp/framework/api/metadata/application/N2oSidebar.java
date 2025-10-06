package net.n2oapp.framework.api.metadata.application;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.aware.DatasourceIdAware;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oStandardDatasource;
import net.n2oapp.framework.api.metadata.jackson.ExtAttributesSerializer;
import net.n2oapp.framework.api.metadata.menu.N2oSimpleMenu;

import java.util.Map;

/**
 * Боковая панель приложения
 */
@Getter
@Setter
public class N2oSidebar extends N2oMetadata implements DatasourceIdAware, ExtensionAttributesAware {
    /**
     * React компонент боковой панели
     */
    private String src;
    /**
     * CSS класс боковой панели
     */
    private String cssClass;
    /**
     * СSS стиль боковой панели
     */
    private String style;
    /**
     * Заголовок боковой панели
     */
    private String title;
    /**
     * Подзаголовок боковой панели
     */
    private String subtitle;
    /**
     * Сторона появления боковой панели
     */
    private SidebarSideEnum side;
    /**
     * Путь, по которому будет отображаться боковая панель
     */
    private String path;
    /**
     * Идентификатор источника данных боковой панели
     */
    private String datasourceId;
    /**
     * Путь к файлу с логотипом, который будет отображаться в боковой панели
     */
    private String logoSrc;
    /**
     * CSS класс логотипа боковой панели
     */
    private String logoClass;
    /**
     * URL страницы, переход на которую происходит по клику на логотип или название
     */
    private String homePageUrl;
    /**
     * Исходное состояние боковой панели
     */
    private SidebarStateEnum defaultState;
    /**
     * Состояние боковой панели принимаемое при нажатии на иконку, задаваемую атрибутом sidebar-toggled-icon шапки
     */
    private SidebarStateEnum toggledState;
    /**
     * Открывается ли панель по hover
     */
    private Boolean toggleOnHover;
    /**
     * Перекрывает ли боковая панель содержимое страницы
     */
    private Boolean overlay;
    @ExtAttributesSerializer
    private Map<N2oNamespace, Map<String, String>> extAttributes;

    /**
     * Встроенный источник данных
     */
    private N2oStandardDatasource datasource;
    /**
     * Основное меню
     */
    private N2oSimpleMenu navMenu;
    /**
     * Дополнительное меню
     */
    private N2oSimpleMenu extraMenu;

    @Override
    public String getPostfix() {
        return "sidebar";
    }

    @Override
    public Class<? extends SourceMetadata> getSourceBaseClass() {
        return N2oSidebar.class;
    }
}
