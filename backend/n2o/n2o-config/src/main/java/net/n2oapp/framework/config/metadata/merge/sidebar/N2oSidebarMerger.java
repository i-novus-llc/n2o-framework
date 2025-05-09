package net.n2oapp.framework.config.metadata.merge.sidebar;

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
    public T merge(T ref, T source) {
        setIfNotNull(source::setSrc, source::getSrc, ref::getSrc);
        setIfNotNull(source::setCssClass, source::getCssClass, ref::getCssClass);
        setIfNotNull(source::setStyle, source::getStyle, ref::getStyle);
        setIfNotNull(source::setSide, source::getSide, ref::getSide);
        setIfNotNull(source::setLogoSrc, source::getLogoSrc, ref::getLogoSrc);
        setIfNotNull(source::setTitle, source::getTitle, ref::getTitle);
        setIfNotNull(source::setPath, source::getPath, ref::getPath);
        setIfNotNull(source::setSubtitle, source::getSubtitle, ref::getSubtitle);
        setIfNotNull(source::setHomePageUrl, source::getHomePageUrl, ref::getHomePageUrl);
        setIfNotNull(source::setLogoClass, source::getLogoClass, ref::getLogoClass);
        setIfNotNull(source::setDatasourceId, source::getDatasourceId, ref::getDatasourceId);
        setIfNotNull(source::setDefaultState, source::getDefaultState, ref::getDefaultState);
        setIfNotNull(source::setToggledState, source::getToggledState, ref::getToggledState);
        setIfNotNull(source::setToggleOnHover, source::getToggleOnHover, ref::getToggleOnHover);
        setIfNotNull(source::setOverlay, source::getOverlay, ref::getOverlay);
        setIfNotNull(source::setMenu, source::getMenu, ref::getMenu);
        setIfNotNull(source::setExtraMenu, source::getExtraMenu, ref::getExtraMenu);
        return source;
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSidebar.class;
    }
}
