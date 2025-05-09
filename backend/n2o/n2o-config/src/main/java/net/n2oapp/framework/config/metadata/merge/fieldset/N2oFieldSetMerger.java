package net.n2oapp.framework.config.metadata.merge.fieldset;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import net.n2oapp.framework.config.metadata.compile.BaseSourceMerger;
import org.springframework.stereotype.Component;

/**
 * Слияние двух филдсетов
 */
@Component
public class N2oFieldSetMerger<T extends N2oFieldSet> implements BaseSourceMerger<T> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oFieldSet.class;
    }

    @Override
    public T merge(T ref, T source) {
        setIfNotNull(source::setId, source::getId, ref::getId);
        setIfNotNull(source::setItems, source::getItems, ref::getItems);
        setIfNotNull(source::setLabel, source::getLabel, ref::getLabel);
        setIfNotNull(source::setDescription, source::getDescription, ref::getDescription);
        setIfNotNull(source::setCssClass, source::getCssClass, ref::getCssClass);
        setIfNotNull(source::setStyle, source::getStyle, ref::getStyle);
        setIfNotNull(source::setSrc, source::getSrc, ref::getSrc);
        setIfNotNull(source::setFieldLabelLocation, source::getFieldLabelLocation, ref::getFieldLabelLocation);
        setIfNotNull(source::setFieldLabelAlign, source::getFieldLabelAlign, ref::getFieldLabelAlign);
        setIfNotNull(source::setFieldLabelWidth, source::getFieldLabelWidth, ref::getFieldLabelWidth);
        setIfNotNull(source::setVisible, source::getVisible, ref::getVisible);
        setIfNotNull(source::setEnabled, source::getEnabled, ref::getEnabled);
        setIfNotNull(source::setDependsOn, source::getDependsOn, ref::getDependsOn);
        setIfNotNull(source::setHelp, source::getHelp, ref::getHelp);
        setIfNotNull(source::setBadge, source::getBadge, ref::getBadge);
        setIfNotNull(source::setBadgeColor, source::getBadgeColor, ref::getBadgeColor);
        setIfNotNull(source::setBadgePosition, source::getBadgePosition, ref::getBadgePosition);
        setIfNotNull(source::setBadgeShape, source::getBadgeShape, ref::getBadgeShape);
        setIfNotNull(source::setBadgeImage, source::getBadgeImage, ref::getBadgeImage);
        setIfNotNull(source::setBadgeImagePosition, source::getBadgeImagePosition, ref::getBadgeImagePosition);
        setIfNotNull(source::setBadgeImageShape, source::getBadgeImageShape, ref::getBadgeImageShape);
        mergeExtAttributes(ref, source);
        return source;
    }
}
