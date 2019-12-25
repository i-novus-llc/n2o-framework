package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.page.SimplePageElementIOv2;
import net.n2oapp.framework.config.io.page.StandardPageElementIOv2;
import net.n2oapp.framework.config.metadata.compile.page.*;
import net.n2oapp.framework.config.reader.page.PageXmlReaderV1;

/**
 * Набор для сборки стандартных страниц
 */
public class N2oPagesPack implements MetadataPack<N2oApplicationBuilder> {
    @Override
    public void build(N2oApplicationBuilder b) {
        b.packs(new N2oPagesIOPack());
        b.compilers(new SimplePageCompiler(), new StandardPageCompiler());
        b.binders(new SimplePageBinder(), new StandardPageBinder());
    }
}
