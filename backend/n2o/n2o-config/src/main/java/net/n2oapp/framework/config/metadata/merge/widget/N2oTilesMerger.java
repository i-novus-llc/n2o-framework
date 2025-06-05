package net.n2oapp.framework.config.metadata.merge.widget;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oForm;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oTiles;
import org.springframework.stereotype.Component;

/**
 * Слияние двух виджетов Плитка
 */
@Component
public class N2oTilesMerger extends N2oWidgetMerger<N2oTiles> {

    @Override
    public N2oTiles merge(N2oTiles ref, N2oTiles source) {
        setIfNotNull(source::setColsSm, source::getColsSm, ref::getColsSm);
        setIfNotNull(source::setColsMd, source::getColsMd, ref::getColsMd);
        setIfNotNull(source::setColsLg, source::getColsLg, ref::getColsLg);
        setIfNotNull(source::setHeight, source::getHeight, ref::getHeight);
        setIfNotNull(source::setWidth, source::getWidth, ref::getWidth);
        setIfNotNull(source::setPagination, source::getPagination, ref::getPagination);
        addIfNotNull(ref, source, N2oTiles::setContent, N2oTiles::getContent);
        return source;
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oTiles.class;
    }
}
