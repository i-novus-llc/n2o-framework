package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.datasource.*;
import net.n2oapp.framework.config.metadata.compile.datasource.BrowserStorageDatasourceCompiler;
import net.n2oapp.framework.config.metadata.compile.datasource.InheritedDatasourceCompiler;
import net.n2oapp.framework.config.metadata.compile.datasource.StandardDatasourceCompiler;
import net.n2oapp.framework.config.metadata.compile.datasource.StompDatasourceCompiler;
import net.n2oapp.framework.config.metadata.merge.datasource.N2oBrowserStorageDatasourceMerger;
import net.n2oapp.framework.config.metadata.merge.datasource.N2oInheritedDatasourceMerger;
import net.n2oapp.framework.config.metadata.merge.datasource.N2oStandardDatasourceMerger;
import net.n2oapp.framework.config.metadata.merge.datasource.N2oStompDatasourceMerger;

public class N2oAllDatasourcesPack implements MetadataPack<N2oApplicationBuilder> {
    @Override
    public void build(N2oApplicationBuilder b) {
        b.packs(new N2oDatasourcesV1IOPack());
        b.compilers(new StandardDatasourceCompiler(),
                new BrowserStorageDatasourceCompiler(),
                new StompDatasourceCompiler(),
                new InheritedDatasourceCompiler());
        b.mergers(new N2oStandardDatasourceMerger(), new N2oStompDatasourceMerger(),
                new N2oBrowserStorageDatasourceMerger(), new N2oInheritedDatasourceMerger());
    }
}
