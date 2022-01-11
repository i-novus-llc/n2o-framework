package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.datasource.DatasourceCompiler;

/**
 * Набор для сборки объектов и выборок
 */
public class N2oAllDataPack implements MetadataPack<N2oApplicationBuilder> {
    @Override
    public void build(N2oApplicationBuilder b) {
        b.packs(new N2oQueriesPack(), new N2oObjectsPack(), new N2oDataProvidersPack());
        b.compilers(new DatasourceCompiler());
    }
}