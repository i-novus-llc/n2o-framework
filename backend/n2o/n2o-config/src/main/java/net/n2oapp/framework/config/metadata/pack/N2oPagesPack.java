package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.page.*;

/**
 * Набор для сборки стандартных страниц
 */
public class N2oPagesPack implements MetadataPack<N2oApplicationBuilder> {

    @Override
    public void build(N2oApplicationBuilder b) {
        b.packs(new N2oPagesIOPack());
        b.compilers(new SimplePageCompiler(), new StandardPageCompiler(), new LeftRightPageCompiler());
        b.binders(new SimplePageBinder(), new StandardPageBinder());
    }
}
