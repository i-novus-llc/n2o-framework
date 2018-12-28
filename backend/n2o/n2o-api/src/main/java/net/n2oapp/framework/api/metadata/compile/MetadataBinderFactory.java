package net.n2oapp.framework.api.metadata.compile;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.factory.MetadataFactory;
import net.n2oapp.framework.api.metadata.Compiled;

/**
 * Фабрика связывателей метаданных с данными {@link MetadataBinder}
 */
public interface MetadataBinderFactory extends MetadataFactory<MetadataBinder> {

    /**
     * Связать метаданные с данными
     *
     * @param compiled Собранные метаданные
     * @param processor Процессор связывания
     * @return Связанные метаданные с данными
     */
    <D extends Compiled> D bind(D compiled, CompileProcessor processor);
}
