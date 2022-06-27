package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.datasource.ApplicationDatasourceIO;
import net.n2oapp.framework.config.io.datasource.BrowserStorageDatasourceIO;
import net.n2oapp.framework.config.io.datasource.StandardDatasourceIO;
import net.n2oapp.framework.config.io.datasource.StompDatasourceIO;
import net.n2oapp.framework.config.metadata.compile.datasource.*;
import net.n2oapp.framework.config.metadata.compile.page.*;

/**
 * Набор для сборки стандартных страниц
 */
public class N2oPagesPack implements MetadataPack<N2oApplicationBuilder> {

    @Override
    public void build(N2oApplicationBuilder b) {
        b.packs(new N2oPagesIOv3Pack(), new N2oPagesIOv4Pack());
        b.ios(new ApplicationDatasourceIO(), new BrowserStorageDatasourceIO(), new StandardDatasourceIO(), new StompDatasourceIO());
        b.compilers(new SimplePageCompiler(),
                new StandardPageCompiler(),
                new LeftRightPageCompiler(),
                new TopLeftRightPageCompiler(),
                new SearchablePageCompiler(),
                new StandardDatasourceCompiler(),
                new ApplicationDatasourceCompiler(),
                new BrowserStorageDatasourceCompiler(),
                new StompDatasourceCompiler());
        b.binders(new SimplePageBinder(), new StandardPageBinder());
        b.mergers(new N2oStandardDatasourceMerger());
    }
}
