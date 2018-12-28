package net.n2oapp.framework.api.metadata.global.view.page;

import net.n2oapp.framework.api.metadata.aware.NameAware;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;

import java.util.List;

/**
 * Абстрактная страница
 */
public abstract class N2oPage extends N2oMetadata implements NameAware {
    private String name;
    private String objectId;
    private String route;
    private String modalSize;
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
     * @return список виджетов
     */
    public abstract List<N2oWidget> getContainers();

    public String getModalWidth() {
        return modalWidth;
    }

    public void setModalWidth(String modalWidth) {
        this.modalWidth = modalWidth;
    }

    public String getMinModalWidth() {
        return minModalWidth;
    }

    public void setMinModalWidth(String minModalWidth) {
        this.minModalWidth = minModalWidth;
    }

    public String getMaxModalWidth() {
        return maxModalWidth;
    }

    public void setMaxModalWidth(String maxModalWidth) {
        this.maxModalWidth = maxModalWidth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getResultContainer() {
        return resultContainer;
    }

    public void setResultContainer(String resultContainer) {
        this.resultContainer = resultContainer;
    }

    public String getFilterContainer() {
        return filterContainer;
    }

    public void setFilterContainer(String filterContainer) {
        this.filterContainer = filterContainer;
    }

    @Override
    public Class<? extends N2oMetadata> getSourceBaseClass() {
        return N2oPage.class;
    }

    public String getModalSize() {
        return modalSize;
    }

    public void setModalSize(String modalSize) {
        this.modalSize = modalSize;
    }
}
