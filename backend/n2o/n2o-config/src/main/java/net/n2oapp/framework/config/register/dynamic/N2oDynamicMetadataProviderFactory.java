package net.n2oapp.framework.config.register.dynamic;

import net.n2oapp.framework.api.register.DynamicMetadataProvider;
import net.n2oapp.framework.api.register.DynamicMetadataProviderFactory;
import net.n2oapp.framework.config.factory.BaseMetadataFactory;
import java.util.Map;

/**
 * Реализация фабрики поставщиков динамических метаданных
 */
public class N2oDynamicMetadataProviderFactory extends BaseMetadataFactory<DynamicMetadataProvider>
        implements DynamicMetadataProviderFactory {

    public N2oDynamicMetadataProviderFactory() {
    }

    public N2oDynamicMetadataProviderFactory(Map<String, DynamicMetadataProvider> beans) {
        super(beans);
    }

    public DynamicMetadataProvider produce(String code) {
        return produce((g, c) -> g.getCode().equals(c), code);
    }

}
