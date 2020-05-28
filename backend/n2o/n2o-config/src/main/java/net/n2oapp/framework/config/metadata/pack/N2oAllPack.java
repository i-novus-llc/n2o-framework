package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.config.N2oApplicationBuilder;

/**
 * Набор всех инструментов для сборки метаданных
 */
public class N2oAllPack implements MetadataPack<N2oApplicationBuilder> {
    @Override
    public void build(N2oApplicationBuilder builder) {
        builder.packs(new N2oSourceTypesPack(),
                new N2oOperationsPack(),
                new N2oLoadersPack(),
                new N2oAllPagesPack(),
                new N2oAllDataPack());
    }
}
