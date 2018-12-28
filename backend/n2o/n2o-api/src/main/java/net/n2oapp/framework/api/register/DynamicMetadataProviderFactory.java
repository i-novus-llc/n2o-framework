package net.n2oapp.framework.api.register;

import net.n2oapp.framework.api.factory.MetadataFactory;

/**
 * Фабрика поставщиков динамических метаданных
 */
public interface DynamicMetadataProviderFactory extends MetadataFactory<DynamicMetadataProvider> {
    DynamicMetadataProvider produce(String code);
}
