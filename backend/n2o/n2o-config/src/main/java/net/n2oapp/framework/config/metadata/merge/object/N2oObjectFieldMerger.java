package net.n2oapp.framework.config.metadata.merge.object;

import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.config.metadata.compile.BaseSourceMerger;

public abstract class N2oObjectFieldMerger<T extends AbstractParameter> implements BaseSourceMerger<T> {

    @Override
    public T merge(T ref, T source) {
        setIfNotNull(source::setMapping, source::getMapping,ref::getMapping);
        setIfNotNull(source::setRequired, source::getRequired,ref::getRequired);
        setIfNotNull(source::setEnabled, source::getEnabled,ref::getEnabled);
        setIfNotNull(source::setNormalize, source::getNormalize,ref::getNormalize);
        return source;
    }
}
