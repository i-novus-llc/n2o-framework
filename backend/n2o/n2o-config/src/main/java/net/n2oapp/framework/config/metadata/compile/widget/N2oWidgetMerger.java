package net.n2oapp.framework.config.metadata.compile.widget;

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
    public T merge(T source, T override) {
        setIfNotNull(source::setId, override::getId);
        setIfNotNull(source::setSrc, override::getSrc);
        setIfNotNull(source::setVisible, override::getVisible);
        setIfNotNull(source::setDatasource, override::getDatasource);
        setIfNotNull(source::setQueryId, override::getQueryId);
        setIfNotNull(source::setObjectId, override::getObjectId);
        setIfNotNull(source::setDependsOn, override::getDependsOn);
        setIfNotNull(source::setFetchOnInit, override::getFetchOnInit);
        setIfNotNull(source::setMasterFieldId, override::getMasterFieldId);
        setIfNotNull(source::setDetailFieldId, override::getDetailFieldId);
        setIfNotNull(source::setDependencyCondition, override::getDependencyCondition);
        setIfNotNull(source::setRoute, override::getRoute);
        setIfNotNull(source::setSize, override::getSize);
        setIfNotNull(source::setUpload, override::getUpload);
        setIfNotNull(source::setName, override::getName);
        setIfNotNull(source::setIcon, override::getIcon);
        setIfNotNull(source::setCssClass, override::getCssClass);
        setIfNotNull(source::setStyle, override::getStyle);
        setIfNotNull(source::setBorder, override::getBorder);
        setIfNotNull(source::setAutoFocus, override::getAutoFocus);
        addIfNotNull(source, override, N2oWidget::setActions, N2oWidget::getActions);
        addIfNotNull(source, override, N2oWidget::setToolbars, N2oWidget::getToolbars);
        addIfNotNull(source, override, N2oWidget::setPreFilters, N2oWidget::getPreFilters);
        mergeExtAttributes(source, override);
        return source;
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oWidget.class;
    }
}
