package net.n2oapp.framework.config.metadata.merge.fieldset;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oMultiFieldSet;
import org.springframework.stereotype.Component;

/**
 * Слияние двух филдсетов с динамическим числом полей
 */
@Component
public class N2oMultiFieldSetMerger extends N2oFieldSetMerger<N2oMultiFieldSet> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oMultiFieldSet.class;
    }

    @Override
    public N2oMultiFieldSet merge(N2oMultiFieldSet ref, N2oMultiFieldSet source) {
        setIfNotNull(source::setChildrenLabel, source::getChildrenLabel, ref::getChildrenLabel);
        setIfNotNull(source::setFirstChildrenLabel, source::getFirstChildrenLabel, ref::getFirstChildrenLabel);
        setIfNotNull(source::setAddButtonLabel, source::getAddButtonLabel, ref::getAddButtonLabel);
        setIfNotNull(source::setRemoveAllButtonLabel, source::getRemoveAllButtonLabel, ref::getRemoveAllButtonLabel);
        setIfNotNull(source::setCanRemoveFirst, source::getCanRemoveFirst, ref::getCanRemoveFirst);
        setIfNotNull(source::setCanAdd, source::getCanAdd, ref::getCanAdd);
        setIfNotNull(source::setCanRemove, source::getCanRemove, ref::getCanRemove);
        setIfNotNull(source::setCanRemoveAll, source::getCanRemoveAll, ref::getCanRemoveAll);
        setIfNotNull(source::setCanCopy, source::getCanCopy, ref::getCanCopy);
        setIfNotNull(source::setPrimaryKey, source::getPrimaryKey, ref::getPrimaryKey);
        setIfNotNull(source::setGeneratePrimaryKey, source::getGeneratePrimaryKey, ref::getGeneratePrimaryKey);
        return source;
    }
}
