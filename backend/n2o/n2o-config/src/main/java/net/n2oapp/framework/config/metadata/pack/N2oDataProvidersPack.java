package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.dataprovider.ClientDataProviderCompiler;

/**
 * Набор для сборки провайдеров данных {@link net.n2oapp.framework.config.io.dataprovider.DataProviderIOv1}
 */
public class N2oDataProvidersPack implements MetadataPack<N2oApplicationBuilder> {
    @Override
    public void build(N2oApplicationBuilder b) {
        b.packs(new N2oDataProvidersIOPack());
        b.compilers(new ClientDataProviderCompiler());
    }
}
