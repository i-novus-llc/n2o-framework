package net.n2oapp.framework.config.metadata.merge.widget;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.config.metadata.compile.BaseSourceMerger;
import org.springframework.stereotype.Component;

/**
 * Слияние двух виджетов
 */
@Component
public class N2oWidgetMerger<T extends N2oWidget> implements BaseSourceMerger<T> {

    @Override
    public T merge(T ref, T source) {
        setIfNotNull(source::setId, source::getId, ref::getId);
        setIfNotNull(source::setSrc, source::getSrc, ref::getSrc);
        setIfNotNull(source::setRoute, source::getRoute, ref::getRoute);
        setIfNotNull(source::setQueryId, source::getQueryId, ref::getQueryId);
        setIfNotNull(source::setObjectId, source::getObjectId, ref::getObjectId);
        setIfNotNull(source::setDatasourceId, source::getDatasourceId, ref::getDatasourceId);
        setIfNotNull(source::setDatasource, source::getDatasource, ref::getDatasource);
        setIfNotNull(source::setFetchOnInit, source::getFetchOnInit, ref::getFetchOnInit);
        setIfNotNull(source::setFetchOnVisibility, source::getFetchOnVisibility, ref::getFetchOnVisibility);
        setIfNotNull(source::setSize, source::getSize, ref::getSize);
        setIfNotNull(source::setCssClass, source::getCssClass, ref::getCssClass);
        setIfNotNull(source::setStyle, source::getStyle, ref::getStyle);
        setIfNotNull(source::setAutoFocus, source::getAutoFocus, ref::getAutoFocus);
        setIfNotNull(source::setUpload, source::getUpload, ref::getUpload);
        setIfNotNull(source::setDependsOn, source::getDependsOn, ref::getDependsOn);
        setIfNotNull(source::setMasterFieldId, source::getMasterFieldId, ref::getMasterFieldId);
        setIfNotNull(source::setDetailFieldId, source::getDetailFieldId, ref::getDetailFieldId);
        setIfNotNull(source::setVisible, source::getVisible, ref::getVisible);
        addIfNotNull(ref, source, N2oWidget::setPreFilters, N2oWidget::getPreFilters);
        addIfNotNull(ref, source, N2oWidget::setActions, N2oWidget::getActions);
        addIfNotNull(ref, source, N2oWidget::setToolbars, N2oWidget::getToolbars);
        addIfNotNull(ref, source, N2oWidget::setDependencies, N2oWidget::getDependencies);
        mergeExtAttributes(ref, source);
        return source;
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oWidget.class;
    }
}
