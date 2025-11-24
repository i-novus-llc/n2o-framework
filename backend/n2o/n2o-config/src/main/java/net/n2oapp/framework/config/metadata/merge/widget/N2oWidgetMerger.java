package net.n2oapp.framework.config.metadata.merge.widget;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.config.metadata.compile.BaseSourceMerger;
import org.springframework.stereotype.Component;

import java.util.*;

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
        mergeToolbar(ref, source);
        addIfNotNull(ref, source, N2oWidget::setDependencies, N2oWidget::getDependencies);
        mergeExtAttributes(ref, source);
        return source;
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oWidget.class;
    }

    private void mergeToolbar(T ref, T source) {
        N2oToolbar[] sourceToolbars = source.getToolbars();
        N2oToolbar[] refToolbars = ref.getToolbars();

        if (refToolbars == null || refToolbars.length == 0) {
            return;
        }

        if (sourceToolbars == null || sourceToolbars.length == 0) {
            source.setToolbars(refToolbars);
            return;
        }

        N2oToolbar[] mergedToolbars = mergeToolbarsWithPlace(sourceToolbars, refToolbars);
        source.setToolbars(mergedToolbars);
    }

    /**
     * Объединяет два массива тулбаров, добавляя элементы из ref в source,
     * если в source нет элементов с таким же place
     */
    private N2oToolbar[] mergeToolbarsWithPlace(N2oToolbar[] source, N2oToolbar[] ref) {
        List<N2oToolbar> result = new ArrayList<>(Arrays.asList(source));
        Set<String> existingPlaces = new HashSet<>();

        for (N2oToolbar toolbar : source) {
            String place = getNormalizedPlace(toolbar.getPlace());
            existingPlaces.add(place);
        }

        for (N2oToolbar toolbar : ref) {
            String place = getNormalizedPlace(toolbar.getPlace());
            if (!existingPlaces.contains(place)) {
                result.add(toolbar);
            }
        }

        return result.toArray(new N2oToolbar[0]);
    }

    private String getNormalizedPlace(String place) {
        return place != null ? place : "topLeft";
    }
}
