package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.io.datasource.*;

public class N2oDatasourcesV1IOPack implements MetadataPack<XmlIOBuilder<?>> {
    @Override
    public void build(XmlIOBuilder<?> b) {
        b.ios(new ApplicationDatasourceIO(),
                new BrowserStorageDatasourceIO(),
                new StandardDatasourceIO(),
                new StompDatasourceIO(),
                new InheritedDatasourceIO());
    }
}
