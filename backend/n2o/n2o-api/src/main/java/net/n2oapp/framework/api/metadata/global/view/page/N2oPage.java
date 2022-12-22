package net.n2oapp.framework.api.metadata.global.view.page;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.N2oAttribute;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.aware.NameAware;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.jackson.ExtAttributesSerializer;

import java.util.List;
import java.util.Map;

/**
 * Абстрактная страница
 */
@Getter
@Setter
public abstract class N2oPage extends N2oMetadata implements NameAware, ExtensionAttributesAware, SourceComponent {
    @N2oAttribute("Имя")
    private String name;
    @N2oAttribute("Заголовок")
    private String title;
    @N2oAttribute("Заголовок вкладки браузера")
    private String htmlTitle;
    private String src;
    private String objectId;
    private String route;
    private String modalSize;
    @N2oAttribute("Отображение заголовка страницы")
    private Boolean showTitle;
    @N2oAttribute("Сss класс")
    private String cssClass;
    @N2oAttribute("Стиль")
    private String style;
    private ReduxModel model;
    private Boolean hasBreadcrumbs;
    @N2oAttribute("Список меню с кнопками")
    private N2oBreadcrumb[] breadcrumbs;
    @ExtAttributesSerializer
    protected Map<N2oNamespace, Map<String, String>> extAttributes;

    public Boolean getHasBreadcrumbs() {
        if (breadcrumbs != null)
            return true;
        return Boolean.TRUE.equals(hasBreadcrumbs);
    }

    @Override
    public String getPostfix() {
        return "page";
    }

    /**
     * Получение списка виджетов на странице
     *
     * @return Список виджетов
     */
    @JsonIgnore
    public abstract List<N2oWidget> getWidgets();

    @Override
    public Class<? extends N2oMetadata> getSourceBaseClass() {
        return N2oPage.class;
    }
}
