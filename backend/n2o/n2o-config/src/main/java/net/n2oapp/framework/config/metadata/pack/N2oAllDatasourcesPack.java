package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.datasource.*;
import net.n2oapp.framework.config.metadata.merge.datasource.N2oStandardDatasourceMerger;

public class N2oAllDatasourcesPack implements MetadataPack<N2oApplicationBuilder> {
    @Override
    public void build(N2oApplicationBuilder b) {
        b.packs(new N2oDatasourcesV1IOPack());
        b.compilers(new StandardDatasourceCompiler(),
                new BrowserStorageDatasourceCompiler(),
                new StompDatasourceCompiler(),
                new InheritedDatasourceCompiler());
        b.mergers(new N2oStandardDatasourceMerger());
        b.binders(new BrowserStorageDatasourceBinder(),
                new InheritedDatasourceBinder());
    }
}
