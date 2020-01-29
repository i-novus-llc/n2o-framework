package net.n2oapp.framework.api.metadata.global.view.page;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.aware.NameAware;
import net.n2oapp.framework.api.metadata.aware.SrcAware;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;

import java.util.List;
import java.util.Map;

/**
 * Абстрактная страница
 */
@Getter
@Setter
public abstract class N2oBasePage extends N2oMetadata implements NameAware, ExtensionAttributesAware, SrcAware {
    private String name;
    private String src;
    private String objectId;
    private String route;
    private String modalSize;
    protected Map<N2oNamespace, Map<String, String>> extAttributes;
    @Deprecated
    private String resultContainer;
    @Deprecated
    private String filterContainer;
    @Deprecated
    private String modalWidth;
    @Deprecated
    private String minModalWidth;
    @Deprecated
    private String maxModalWidth;

    @Override
    public String getPostfix() {
        return "page";
    }

    /**
     * Получение списка виджетов на странице
     *
     * @return список виджетов
     */
    public abstract List<N2oWidget> getContainers();

    @Override
    public Class<? extends N2oMetadata> getSourceBaseClass() {
        return N2oBasePage.class;
    }
}
