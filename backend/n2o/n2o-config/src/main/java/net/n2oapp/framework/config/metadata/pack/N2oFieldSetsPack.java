package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.fieldset.*;
import net.n2oapp.framework.config.metadata.compile.widget.FieldSetBinder;
import net.n2oapp.framework.config.metadata.compile.widget.StandardFieldBinder;

public class N2oFieldSetsPack implements MetadataPack<N2oApplicationBuilder> {
    @Override
    public void build(N2oApplicationBuilder b) {
        b.packs(new N2oFieldSetsIOPack(), new N2oFieldSetsV5IOPack());
        b.compilers(new SetFieldSetCompiler(),
                new LineFieldSetCompiler(),
                new MultiFieldSetCompiler(),
                new FieldSetRowCompiler(),
                new FieldSetColumnCompiler());
        b.mergers(new N2oFieldSetMerger<>(), new N2oLineFieldSetMerger());
        b.binders(new FieldSetBinder(),
                new StandardFieldBinder());
    }
}
