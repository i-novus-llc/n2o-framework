package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.fieldset.*;
import net.n2oapp.framework.config.metadata.compile.widget.FieldSetBinder;
import net.n2oapp.framework.config.metadata.compile.widget.StandardFieldBinder;
import net.n2oapp.framework.config.metadata.merge.fieldset.N2oFieldSetMerger;
import net.n2oapp.framework.config.metadata.merge.fieldset.N2oLineFieldSetMerger;
import net.n2oapp.framework.config.metadata.merge.fieldset.N2oMultiFieldSetMerger;

public class N2oFieldSetsPack implements MetadataPack<N2oApplicationBuilder> {
    @Override
    public void build(N2oApplicationBuilder b) {
        b.packs(new N2oFieldSetsIOPack(), new N2oFieldSetsV5IOPack());
        b.compilers(new SetFieldSetCompiler(),
                new LineFieldSetCompiler(),
                new MultiFieldSetCompiler(),
                new FieldSetRowCompiler(),
                new FieldSetColumnCompiler());
        b.mergers(new N2oFieldSetMerger<>(),
                new N2oLineFieldSetMerger(),
                new N2oMultiFieldSetMerger());
        b.binders(new FieldSetBinder(),
                new StandardFieldBinder());
    }
}
