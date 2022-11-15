package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.events.OnChangeEventCompiler;
import net.n2oapp.framework.config.metadata.compile.events.StompEventCompiler;

public class N2oAllEventsPack implements MetadataPack<N2oApplicationBuilder> {
    @Override
    public void build(N2oApplicationBuilder b) {
        b.packs(new N2oEventsIOPack());
        b.compilers(new OnChangeEventCompiler(), new StompEventCompiler());
    }
}
