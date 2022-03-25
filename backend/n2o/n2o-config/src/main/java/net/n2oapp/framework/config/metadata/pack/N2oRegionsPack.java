package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.region.*;


/**
 * Набор для сборки стандартных регионов
 */
public class N2oRegionsPack implements MetadataPack<N2oApplicationBuilder> {
    @Override
    public void build(N2oApplicationBuilder b) {
        b.packs(new N2oRegionsV2IOPack(), new N2oRegionsV3IOPack());
        b.compilers(new TabsRegionCompiler(),
                new NoneRegionCompiler(),
                new PanelRegionCompiler(),
                new LineRegionCompiler(),
                new CustomRegionCompiler(),
                new ScrollspyRegionCompiler());
    }
}
