package net.n2oapp.framework.config.metadata.merge.fieldset;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oLineFieldSet;
import org.springframework.stereotype.Component;

/**
 * Слияние двух филдсетов с горизонтальной линией
 */
@Component
public class N2oLineFieldSetMerger extends N2oFieldSetMerger<N2oLineFieldSet> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oLineFieldSet.class;
    }

    @Override
    public N2oLineFieldSet merge(N2oLineFieldSet ref, N2oLineFieldSet source) {
        setIfNotNull(source::setExpand, source::getExpand, ref::getExpand);
        setIfNotNull(source::setCollapsible, source::getCollapsible, ref::getCollapsible);
        setIfNotNull(source::setHasSeparator, source::getHasSeparator, ref::getHasSeparator);
        return source;
    }
}
