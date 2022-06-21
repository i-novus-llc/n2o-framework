package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.datasource.BrowserStorageDatasourceIO;
import net.n2oapp.framework.config.io.datasource.StandardDatasourceIO;
import net.n2oapp.framework.config.io.datasource.StompDatasourceIO;
import net.n2oapp.framework.config.metadata.compile.datasource.BrowserStorageDatasourceCompiler;
import net.n2oapp.framework.config.metadata.compile.datasource.N2oStandardDatasourceMerger;
import net.n2oapp.framework.config.metadata.compile.datasource.StandardDatasourceCompiler;
import net.n2oapp.framework.config.metadata.compile.datasource.StompDatasourceCompiler;
import net.n2oapp.framework.config.metadata.compile.page.*;

/**
 * Набор для сборки стандартных страниц
 */
public class N2oPagesPack implements MetadataPack<N2oApplicationBuilder> {

    @Override
    public void build(N2oApplicationBuilder b) {
        b.packs(new N2oPagesIOv3Pack(), new N2oPagesIOv4Pack());
        b.ios(new BrowserStorageDatasourceIO(), new StandardDatasourceIO(), new StompDatasourceIO());
        b.compilers(new SimplePageCompiler(),
                new StandardPageCompiler(),
                new LeftRightPageCompiler(),
                new TopLeftRightPageCompiler(),
                new SearchablePageCompiler(),
                new StandardDatasourceCompiler(),
                new BrowserStorageDatasourceCompiler(),
                new StompDatasourceCompiler());
        b.binders(new SimplePageBinder(), new StandardPageBinder());
        b.mergers(new N2oStandardDatasourceMerger());
    }
}
