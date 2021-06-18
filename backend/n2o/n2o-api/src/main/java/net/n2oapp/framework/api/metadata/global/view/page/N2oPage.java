package net.n2oapp.framework.api.metadata.global.view.page;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.aware.NameAware;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;

import java.util.List;
import java.util.Map;

/**
 * Абстрактная страница
 */
@Getter
@Setter
public abstract class N2oPage extends N2oMetadata implements NameAware, ExtensionAttributesAware, SourceComponent {
    private String name;
    private String title;
    private String htmlTitle;
    private String src;
    private String objectId;
    private String route;
    private String modalSize;
    private Boolean showTitle;
    private String cssClass;
    private String style;
    protected Map<N2oNamespace, Map<String, String>> extAttributes;


    @Override
    public String getPostfix() {
        return "page";
    }

    /**
     * Получение списка виджетов на странице
     *
     * @return Список виджетов
     */
    public abstract List<N2oWidget> getWidgets();

    @Override
    public Class<? extends N2oMetadata> getSourceBaseClass() {
        return N2oPage.class;
    }
}
