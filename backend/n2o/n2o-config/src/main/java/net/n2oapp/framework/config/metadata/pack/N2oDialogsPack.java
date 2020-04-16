package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.action.DialogBinder;
import net.n2oapp.framework.config.metadata.compile.dialog.DialogCompiler;

/**
 * Набор для сборки диалогов
 */
public class N2oDialogsPack implements MetadataPack<N2oApplicationBuilder> {
    @Override
    public void build(N2oApplicationBuilder b) {
        b.compilers(new DialogCompiler());
        b.binders(new DialogBinder());
    }
}
