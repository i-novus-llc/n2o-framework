package net.n2oapp.framework.config.metadata.merge.datasource;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.application.N2oStompDatasource;
import net.n2oapp.framework.config.metadata.compile.BaseSourceMerger;
import org.springframework.stereotype.Component;

/**
 * Слияние двух STOMP источников данных
 */
@Component
public class N2oStompDatasourceMerger implements BaseSourceMerger<N2oStompDatasource> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oStompDatasource.class;
    }

    @Override
    public N2oStompDatasource merge(N2oStompDatasource source, N2oStompDatasource override) {
        setIfNotNull(source::setDestination, override::getDestination);
        setIfNotNull(source::setValues, override::getValues);
        return source;
    }
}
