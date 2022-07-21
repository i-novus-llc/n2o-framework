package net.n2oapp.framework.config.metadata.merge.datasource;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oInheritedDatasource;
import org.springframework.stereotype.Component;

/**
 * Слияние двух источников данных, получающих данные из другого источника данных
 */
@Component
public class N2oInheritedDatasourceMerger extends N2oDatasourceMerger<N2oInheritedDatasource> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oInheritedDatasource.class;
    }

    @Override
    public N2oInheritedDatasource merge(N2oInheritedDatasource source, N2oInheritedDatasource override) {
        super.merge(source, override);
        setIfNotNull(source::setSourceDatasource, override::getSourceDatasource);
        setIfNotNull(source::setSourceModel, override::getSourceModel);
        setIfNotNull(source::setSourceFieldId, override::getSourceFieldId);
        setIfNotNull(source::setSubmit, override::getSubmit);
        return source;
    }
}
