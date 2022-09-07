package net.n2oapp.framework.config.metadata.compile.application.sidebar;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.application.N2oSidebar;
import net.n2oapp.framework.config.metadata.compile.BaseSourceMerger;
import org.springframework.stereotype.Component;

/**
 * Слияние двух боковых панелей
 */
@Component
public class N2oSidebarMerger<T extends N2oSidebar> implements BaseSourceMerger<T> {

    @Override
    public T merge(T source, T override) {
        setIfNotNull(source::setVisible, override::getVisible);
        setIfNotNull(source::setSide, override::getSide);
        setIfNotNull(source::setLogoSrc, override::getLogoSrc);
        setIfNotNull(source::setTitle, override::getTitle);
        setIfNotNull(source::setPath, override::getPath);
        setIfNotNull(source::setSubtitle, override::getSubtitle);
        setIfNotNull(source::setHomePageUrl, override::getHomePageUrl);
        setIfNotNull(source::setLogoClass, override::getLogoClass);
        setIfNotNull(source::setDatasourceId, override::getDatasourceId);
        setIfNotNull(source::setDefaultState, override::getDefaultState);
        setIfNotNull(source::setToggledState, override::getToggledState);
        setIfNotNull(source::setToggleOnHover, override::getToggleOnHover);
        setIfNotNull(source::setOverlay, override::getOverlay);
        setIfNotNull(source::setMenu, override::getMenu);
        setIfNotNull(source::setExtraMenu, override::getExtraMenu);
        return source;
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSidebar.class;
    }
}
