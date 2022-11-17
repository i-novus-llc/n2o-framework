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
    public N2oMultiFieldSet merge(N2oMultiFieldSet source, N2oMultiFieldSet override) {
        setIfNotNull(source::setChildrenLabel, override::getChildrenLabel);
        setIfNotNull(source::setFirstChildrenLabel, override::getFirstChildrenLabel);
        setIfNotNull(source::setAddButtonLabel, override::getAddButtonLabel);
        setIfNotNull(source::setRemoveAllButtonLabel, override::getRemoveAllButtonLabel);
        setIfNotNull(source::setCanRemoveFirst, override::getCanRemoveFirst);
        setIfNotNull(source::setCanAdd, override::getCanAdd);
        setIfNotNull(source::setCanRemove, override::getCanRemove);
        setIfNotNull(source::setCanRemoveAll, override::getCanRemoveAll);
        setIfNotNull(source::setCanCopy, override::getCanCopy);
        return source;
    }
}
